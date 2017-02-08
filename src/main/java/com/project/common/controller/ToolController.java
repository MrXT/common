
package com.project.common.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.util.MapDistanceUtils;


/**
 * 系统工具（正向生成，地图，构建表单，统计页面,文件上传，接口测试）
 * ClassName: LoginController <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月20日
 * @version 1.0
 */
@RequestMapping("/tool")
@Controller
@ApiIgnore
public class ToolController {
    /**去接口测试页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/interfaceTest")
    public String interfaceTest(){
        return "tool/interface_test";
    }
    @RequestMapping(value="/goFormbuilder")
    public String goFormbuilder(){
        return "tool/form_builder";
    }
    /**
     * 正向生成代码
     * @return
     */
    @RequestMapping(value="/createCode")
    public String createCode(){
        return "tool/createCode";
    }
    
    /**地图页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/map")
    public String map(){
        return "tool/map";
    }
    
    /**获取地图坐标页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/mapXY")
    public String mapXY(){
        return "tool/mapXY";
    }
    /**
     *  根据经纬度计算距离
     * @param args
     * @throws Exception
     */
    @RequestMapping(value="/getDistance")
    @ResponseBody
    public Object getDistance(String ZUOBIAO_Y,String ZUOBIAO_X,String ZUOBIAO_Y2,String ZUOBIAO_X2){
        return MapDistanceUtils.getDistance(ZUOBIAO_Y,ZUOBIAO_X,ZUOBIAO_Y2,ZUOBIAO_X2);
    }
    /**图表报表demo页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/fusionchartsdemo")
    public String fusionchartsdemo(Map<String,Object> map){
        String strXML = "<graph caption='前12个月订单销量柱状图' xAxisName='月份' yAxisName='值' decimalPrecision='0' formatNumberScale='0'>"
                        +"<set name='2013-05' value='4' color='AFD8F8'/>"
                        +"<set name='2013-04' value='1' color='AFD8F8'/>"
                        +"<set name='2013-03' value='1' color='AFD8F8'/>"
                        +"<set name='2013-02' value='1' color='AFD8F8'/>"
                        +"<set name='2013-01' value='1' color='AFD8F8'/>"
                        +"<set name='2012-01' value='1' color='AFD8F8'/>"
                        +"<set name='2012-11' value='1' color='AFD8F8'/>"
                        +"<set name='2012-10' value='1' color='AFD8F8'/>"
                        +"<set name='2012-09' value='1' color='AFD8F8'/>"
                        +"<set name='2012-08' value='1' color='AFD8F8'/>"
                        +"<set name='2012-07' value='1' color='AFD8F8'/>"
                        +"<set name='2012-06' value='1' color='AFD8F8'/>"
                        +"</graph>" ;
        map.put("strXML", strXML);
        return "tool/fusionchartsdemo";
    }
    /**打印测试页面
     * @return
     */
    @RequestMapping(value="/printTest")
    public String printTest(){
        return "tool/printTest";
    }
    /**打印预览页面
     * @return
     */
    @RequestMapping(value="/printPage")
    public String printPage(){
        return "tool/printPage";
    }
}

