package com.project.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.annotation.ApiResponseDesc;
import com.project.common.annotation.ApiResponseDescs;
import com.project.common.bean.ftp.FTPClientTemplate;
import com.project.common.constant.FileTypeEnum;
import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.exception.ServerException;
import com.project.common.util.DateUtils;
import com.project.common.util.EncodeDecodeUtils;
import com.project.common.util.IoUtils;
import com.project.common.util.ListUtils;
import com.project.common.util.QRCodeUtils;
import com.project.common.util.ValidateUtils;

@Controller
@RequestMapping("common")
@Api("公共-接口")
public class CommonController {

    @Autowired
    private FTPClientTemplate ftpClient;

    /**
     * 文件上传(支持批量)
     */
    @RequestMapping(value = "file/upload", method = RequestMethod.POST)
    @ApiOperation(value="文件上传",notes="可以批量上传",response=List.class)
    @ApiImplicitParam(dataType="File",value="文件",name="files",allowMultiple=true,paramType="form")
    @ApiResponseDescs({
        @ApiResponseDesc
    })
    @ResponseBody
    public Object upload(@RequestParam("files") MultipartFile[] files) {
        if (ListUtils.isEmpty(files)) {
            throw new BusinessException(SystemConstant.NULL_FILE);
        }
        List<String> filePaths = new ArrayList<String>();
        for (MultipartFile file : files) {
            StringBuilder filePath = new StringBuilder();
            FileTypeEnum fileTypeEnum = null;
            String fileName = file.getOriginalFilename().toLowerCase();
            if (fileName.matches("/[^\\s]+\\.(jpg|gif|png|bmp)/i")) {
                fileTypeEnum = FileTypeEnum.PIC;
            } else if (fileName.endsWith(".apk")) {
                fileTypeEnum = FileTypeEnum.APK;
            } else {
                fileTypeEnum = FileTypeEnum.FILE;
            }
            filePath.append(fileTypeEnum + "/" + DateUtils.toFormatDateString(new Date(), "yyyyMMdd") + "/");
            if (ftpClient.mkdir(filePath.toString())) {
                    try {
                        if (ftpClient.put(filePath.toString() + EncodeDecodeUtils.md5Digest(file.getOriginalFilename()),file.getInputStream())) {
                            filePaths.add(filePath.append(file.getOriginalFilename()).toString());
                        }
                    } catch (Exception e) {
                        throw new ServerException(e);
                    }
            }
        }
        return filePaths;
    }

    /**
     * 文件下载
     * @throws Exception
     */
    @ApiOperation(value="文件下载")
    @RequestMapping(value="file/download",method=RequestMethod.GET)
    public void download(@RequestParam String filePath, @ApiIgnore HttpServletResponse response){
        BufferedOutputStream bos = null;
        if (ValidateUtils.isBlank(filePath)) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        String fileName = null;
        if (filePath.indexOf("/") == -1) {
            fileName = filePath;
        } else {
            fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        }
        try {
            String downloadPath = filePath.substring(0, filePath.lastIndexOf("/") + 1) + EncodeDecodeUtils.md5Digest(fileName);
            response.setContentLength((int)ftpClient.getFileSize(downloadPath));//支持迅雷下载
            bos = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ftpClient.get(downloadPath, bos);
        } catch (Exception e) {
            if(e instanceof ServerException){
                throw new ServerException(e.getMessage());
            }else if(e instanceof BusinessException){
                throw new BusinessException(e.getMessage());
            }
        }finally{
            IoUtils.closeQuietly(bos);
        }
       
    }

    /**
     * 图片展示
     * @throws Exception
     */
    @ApiOperation(value="图片展示")
    @RequestMapping(value = "show",method=RequestMethod.GET)
    public void show(@RequestParam String filePath, @ApiIgnore HttpServletResponse response){
        BufferedOutputStream bos = null;
        if (ValidateUtils.isBlank(filePath)) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        String fileName = null;
        if (filePath.indexOf("/") == -1) {
            fileName = filePath;
        } else {
            fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        }
        try {
            String path = filePath.substring(0, filePath.lastIndexOf("/") + 1) + EncodeDecodeUtils.md5Digest(fileName);
            ftpClient.getFileSize(path);
            response.setContentType("image/jpeg");
            bos = new BufferedOutputStream(response.getOutputStream());
            ftpClient.get(path, bos);
        } catch (Exception e) {
            if(e instanceof ServerException){
                throw new ServerException(e.getMessage());
            }else if(e instanceof BusinessException){
                throw new BusinessException(e.getMessage());
            }
        }finally{
            IoUtils.closeQuietly(bos);
        }
    }

    /**
     * 输入内容返回二维码
     * @param content
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="二维码生成")
    @RequestMapping(value = "qrcode",method=RequestMethod.GET)
    public void qr(@RequestParam String content,@ApiIgnore HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        QRCodeUtils.writeToStream(content, response.getOutputStream());
    }
    /**
     * session失效
     * @return
     */
    @RequestMapping("/nosession")
    @ResponseBody
    @ApiIgnore
    public Object nosession(){
        throw new BusinessException(SystemConstant.SESSION_INVALID,"SESSION失效！");
    }
    /**
     * 没权限
     * @return
     */
    @RequestMapping("/noauth")
    @ResponseBody
    @ApiIgnore
    public Object noauth(){
        throw new BusinessException(SystemConstant.NO_AUTH,"没有权限！");
    }
}
