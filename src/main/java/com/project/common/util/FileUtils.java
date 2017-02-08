package com.project.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件工具类
 * @author XT
 * @version 2011-08
 */
public class FileUtils {

	private static transient Log logger = LogFactory.getLog(FileUtils.class);

	private static File file;

	/**
	 * 生成随机文件名
	 * @param suffix 文件名后缀
	 */
	public static String getRandomFileName(String suffix){
		return java.util.UUID.randomUUID().toString()+suffix;
	}
	
	/**
	 * 创建文件
	 * @param filePath 需创建的文件目录路径
	 */
	public static void createFile(String filePath) {
		try {
			file = new File(filePath);
			if (file.exists()) {
				logger.info(filePath + "文件已存在");
			} else {
				logger.info(filePath + "文件不存在，尝试创建");
				if (!file.createNewFile()) {
					logger.error(filePath + "文件创建失败，请确认路径存在,磁盘没有写保护并且空件足够");
					System.exit(1);
				}
				logger.info(filePath + "创建成功");
			}
		} catch (Exception e) {
			System.err.println("文件创建发生异常");
			logger.error(filePath + "文件创建发生异常");
			logger.error(e.getMessage());
		}
	}

	/**
	 * 删除指定文件夹及其子文件
	 * @param fileDir 需删除的文件夹路径
	 */
	public static void delAllFiles(String fileDir) {
		// 指定删除目录路径构造一个文件对象
		file = new File(fileDir);
		File[] fileList = file.listFiles();
		// 初始化子目录路径
		String dirPath = null;
		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				// 如果是文件就将其删除
				if (fileList[i].isFile())
					fileList[i].delete();
				// 如果是目录,那么将些目录下所有文件删除后再将其目录删除,
				if (fileList[i].isDirectory()) {
					dirPath = fileList[i].getPath();
					// 递归删除指定目录下所有文件
					delAllFiles(dirPath);
				}
			}
		}
		// 删除给定根目录
		file.delete();
		logger.info(fileDir + "目录下的文件删除成功");
	}
	
	/**
	 * 读取文件内容
	 * @param filePath 读取文件的目录路径
	 */
	public static String readFile(String filePath) {
	    StringBuilder content = null;
		try {
			file = new File(filePath);
			if (file.exists()) {
				content = new StringBuilder();
				BufferedReader input = new BufferedReader(new FileReader(file));
				String eachLineContent = new String();
				while ((eachLineContent = input.readLine()) != null) {
					content = content.append(eachLineContent + "\n");
				}
				input.close();
			} else {
				logger.error(filePath + "文件不存在");
			}
		} catch (Exception e) {
			logger.error("写入" + filePath + "失败");
			logger.error(e.getMessage());
		}
		return content==null?null:content.toString();
	}
	
    /**
     * 读取指定文件夹下所有文件
     * @param fileDir 文件夹路径
     */
    public static File[] readAllFiles(String fileDir) {
        // 指定删除目录路径构造一个文件对象
        file = new File(fileDir);
        return file.listFiles();
    }

	/**
	 * 写文件<br>
	 * 如果文件不存在则创建该文件
	 * @param filePath 写入文件的目录路径
	 * @param content  写入文件的内容
	 */
	public static void writeFile(String filePath, String content) {

		try {
			String oldContent = readFile(filePath);
			if(oldContent==null){
				createFile(filePath);
			}
			String newContent = oldContent + content;
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(newContent.toString());
			output.close();
		} catch (Exception e) {
			logger.error("写入" + filePath + "失败");
			logger.error(e.getMessage());
		}
	}

    /**
     * @description: 写入文件
     * @author ZZW
     * @param f
     * @param is
     * @throws IOException
     */
    public static void writeFileWithStream(File f, InputStream is) throws IOException{
        logger.debug("begin upload file:"+f.getAbsolutePath());
        OutputStream os = null;
        if (!f.exists()){
            f.createNewFile();
        }
        else{
            f.delete();
            f.createNewFile();
        }
        os = new FileOutputStream(f);
        byte buffer[] = new byte[4 * 1024];
        while (is.read(buffer) != -1){
            os.write(buffer);
        }
        logger.debug("end upload file:"+f.getAbsolutePath());
        if(os != null){
            os.close();
        }
    }
}
