
package com.project.common.spring.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.project.common.util.DateUtils;

/**
 * 日期格式转化
 * @author WJK
 * @version:1.0 2014-09
 */
public class DateConvertEditor extends PropertyEditorSupport {
    private static transient Logger logger = LoggerFactory.getLogger(DateConvertEditor.class);
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                if (text.indexOf(":") == -1 && text.length() == 10) { //yyyy-MM-dd
                    setValue(DateUtils.parseShortDateString(text));
                } else if (text.indexOf(":") == -1 && text.length() == 11) {//'yyyy年MM月dd日'
                    setValue(DateUtils.parseShortGBDateString(text));
                } else if(text.indexOf(":")  > 0 && text.length() == 5){//'HH:mm'
                    setValue(DateUtils.parser(text, "HH:mm"));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {//yyyy-MM-dd hh:mm:ss
                    setValue(DateUtils.parseLongDateString(text));
                }else if (text.indexOf(":") > 0 && text.length() == 16) {//yyyy-MM-dd hh:mm
                    setValue(DateUtils.parseAppDateString(text));
                }else{
                    String errorMsg = "Could not parse date, date format is error "+text;
                    logger.error(errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
            } catch (ParseException ex) {
                IllegalArgumentException iae = new IllegalArgumentException(text + "Could not parse date: " + ex.getMessage());
                iae.initCause(ex);
                logger.error(iae.getMessage());
                throw iae;
            }
        } else {
            setValue(null);
        }
    }
}

