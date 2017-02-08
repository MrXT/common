
package com.project.common.bean;

import org.springframework.stereotype.Component;
/**
 * 异步方法调用例子
 * ClassName: Async <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月29日
 * @version 1.0
 */
@Component
public class Async {
    @org.springframework.scheduling.annotation.Async
    public void print(){
        try {
            Thread.sleep(1000*4);
        } catch (InterruptedException e) {
        }
        System.out.println("呵呵");
    } 
}

