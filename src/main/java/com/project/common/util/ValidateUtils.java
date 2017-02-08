package com.project.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
/**
 * <p> 验证工具类 </p>
 * @author XT
 * @version 2010-11
 */
public class ValidateUtils {

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }
	
	public static boolean isNotPureNumber(String str){   
		return !isPureNumber(str);
	}
	
	public static boolean isNotEmailFormat(String str) {
		return !isEmailFormat(str);
	}

	public static boolean isNotMobileNo(String str){   
		return !isMobileNo(str);
	}
	
	public static boolean isNotLegal(String str){   
		return !isLegal(str);
	}
	
	/**   
	 * 判断输入字符串是否为空字符串（包括空格）
	 * @return 为空true反之false 
	 */  
	public static final boolean isBlank(String str) {
//      int strLen;
//      if(str == null || (strLen = str.length()) == 0)
//          return true;
//      for(int i = 0; i < strLen; i++)
//          if(!Character.isWhitespace(str.charAt(i)))
//              return false;
//
//      return true;
		return StringUtils.isBlank(str);
	}
	
	/**   
	 * 判断输入字符串是否为空字符串（不包括空格）
	 * @return 为空true反之false 
	 */  
    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

	
	/**   
	 * 判断输入字符串是否符合规范
	 * 即判断输入字符是否包含双字节（如：中文，日语，全角等）
	 * @return 包含双字节字符返回false,否则true 
	 */   
	public static boolean isLegal(String str){   
		boolean flag = true;
		if(str!=null){
			for(int i=0;i<str.length();i++){
				String charStr = String.valueOf(str.charAt(i));
				//String regEx = "[\u4e00-\u9fa5]";//中文
				String regEx = "[^\\x00-\\xff]*";//双字节
				if(isMatch(charStr,regEx))
					return !flag;
			}
		}
		return flag;
	}

	/**   
	 * 判断输入字符串是否为纯数字
	 * @return 是纯数字返回true反之false 
	 */  
	public static boolean isPureNumber(String str){   
		return isMatch(str,"^[0-9]+$");
	}
	
	/**   
	 * 判断输入字符串是否满足邮箱格式
	 * @return 满足返回true反之false 
	 */  
	public static boolean isEmailFormat(String str) {
		return isMatch(str,"\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}
	
	/**   
	 * 判断输入字符串是否满足手机号格式<br>
	 * 手机号格式为：13开头的11位纯数字
	 * @return 满足返回true反之false 
	 */ 
	public static boolean isMobileNo(String str){   
	    return isMatch(str, "^(13[0-9]|15[0-9]|18[0-9])\\d{8}$");
	}
	
    /**
     * 电话号码验证
     * 
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) { 
        Pattern p1 = null,p2 = null;
        Matcher m = null;
        boolean b = false;  
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if(str.length() >9)
        {   m = p1.matcher(str);
            b = m.matches();  
        }else{
            m = p2.matcher(str);
            b = m.matches(); 
        }  
        return b;
    }
	
	/**
	 * 判断输入字符串是否为IP地址
	 * @return 是则返回true反之false 
	 */
	public static boolean isIPAddress(String str){
 		return isMatch(str,"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
	}
	
	/**
	 * 利用正则表达式检查是否完整匹配
	 * @param str 校验字符
	 * @param reg 正表表达式
	 * @return
	 */
	public static boolean isMatch(String str,String reg){
		if (isNotEmpty(str)&&isNotEmpty(reg)){
			Pattern p = Pattern.compile(reg); 
			Matcher m = p.matcher(str);
			return m.matches();
		}
 		return false;
	}
	
	/**
	 * 利用正则表达式检查是否包含
	 * @param str 校验字符
	 * @param reg 正表表达式
	 * @return
	 */
	public static boolean isContain(String text,String reg){
		if (StringUtils.isNotEmpty(text)&&StringUtils.isNotEmpty(reg)){
			Pattern p = Pattern.compile(reg); 
			Matcher m = p.matcher(text);
			return m.find();
		}
 		return false;
	}
	
    /**
     * @description: 金额验证
     * @author ZZW
     * @param str
     * @return
     */
    public static boolean isAmount(String str) 
    { 
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(str); 
        if(match.matches()==false) 
        { 
           return false; 
        } 
        else 
        { 
           return true; 
        } 
    }
}
