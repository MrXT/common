package com.project.common.spring.initializer;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import com.project.common.spring.propertyeditor.DateConvertEditor;
import com.project.common.spring.propertyeditor.ListJsonConvertEditor;

/**
 * 指定日期类型转化格式
 * @author XT
 * @version:1.0 2014-09
 */
public class MyWebBinding implements WebBindingInitializer {
    
    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(List.class, new ListJsonConvertEditor(List.class));
	}
}
