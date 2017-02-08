package com.project.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * 日期工具类 
 * @author WJK
 * @version 2014-11
 */
public class DateUtils {
    
	/**
	 * 将一个日期型转换为指定格式字串
	 * 
	 * @param aDate
	 * @param formatStr
	 * @return
	 */
	public static String toFormatDateString(Date aDate, String formatStr) {
	    SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		if (aDate == null) { // 对异常aData==null的处理
			return "";
		}
		return formatter.format(aDate);
	}

	/**
	 * 将一个日期型转换为'yyyy-MM-dd'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toShortDateString(Date aDate) {
		return toFormatDateString(aDate, "yyyy-MM-dd");
	}

	/**
	 * 将一个日期型转换为'yyyyMMddHHmm'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toMailDateString(Date aDate) {
		return toFormatDateString(aDate, "yyyyMMddHHmm");
	}

	/**
	 * 将一个日期型转换为'yyyy-MM-dd HH:mm:ss'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toLongDateString(Date aDate) {
		return toFormatDateString(aDate, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将一个日期型转换为'yyyy年MM月dd日'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toShortDateGBString(Date aDate) {
		return toFormatDateString(aDate, "yyyy'年'MM'月'dd'日'");
	}

	/**
	 * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分ss秒'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toLongDateGBString(Date aDate) {
		return toFormatDateString(aDate, "yyyy'年'MM'月'dd'日' HH'时'mm'分'ss'秒'");
	}

	/**
	 * 将一个日期型转换为'yyyy-MM-dd HH:mm:ss:SSS'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toFullDateString(Date aDate) {
		return toFormatDateString(aDate, "yyyy-MM-dd HH:mm:ss:SSS");
	}

	/**
	 * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分ss秒SSS毫秒'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toFullDateGBString(Date aDate) {
		return toFormatDateString(aDate,
				"yyyy'年'MM'月'dd'日' HH'时'mm'分'ss'秒'SSS'毫秒'");
	}

	/**
	 * 将一个日期型转换为'yyyyMMddHHmmssSSS'格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toFullDateCompactString(Date aDate) {
		return toFormatDateString(aDate, "yyyyMMddHHmmssSSS");
	}

	/**
	 * 将一个日期型转换为LDAP格式字串
	 * 
	 * @param aDate
	 * @return
	 */
	public static String toLDAPDateString(Date aDate) {
		return toFormatDateString(aDate, "yyyyMMddHHmm'Z'");
	}

	/**
	 * 将一个符合指定格式的字串解析成日期型
	 * 
	 * @param strDate
	 * @param formatter
	 * @return
	 * @throws ParseException
	 */
	public static Date parser(String strDate, String formatter)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.parse(strDate);

	}

	/**
	 * 将一个符合'yyyy-MM-dd HH:mm:ss'格式的字串解析成日期型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date parseLongDateString(String strDate)
			throws ParseException {
		return parser(strDate, "yyyy-MM-dd HH:mm:ss");

	}
	
	/**
	 * 将一个符合'yyyy-MM-dd HH:mm'格式的字串解析成日期型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date parseAppDateString(String strDate)
	    throws ParseException {
	    return parser(strDate, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 将一个符合'yyyy-MM-dd'格式的字串解析成日期型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date parseShortDateString(String strDate)
			throws ParseException {
		return parser(strDate, "yyyy-MM-dd");

	}

	/**
	 * 将一个符合'yyyyMMddHHmm'格式的字串解析成日期型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date parseMailDateString(String strDate)
			throws ParseException {
		return parser(strDate, "yyyyMMddHHmm");

	}

	/**
	 * 将一个符合'yyyy-MM-dd HH:mm:ss:SSS'格式的字串解析成日期型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date parseFullDateString(String strDate)
			throws ParseException {
		return parser(strDate, "yyyy-MM-dd HH:mm:ss:SSS");

	}

	/**
	 * 为一个日期加上指定年数
	 * 
	 * @param myDate
	 * @param amount
	 *            年数
	 * @return
	 */
	public static Date addYear(Date myDate, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.YEAR, amount);
		return cal.getTime();
	}

	/**
	 * 为一个日期加上指定月数
	 * 
	 * @param myDate
	 * @param amount
	 *            月数
	 * @return
	 */
	public static Date addMonth(Date myDate, int amount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(myDate);
        cal.add(GregorianCalendar.MONTH, amount);
        return cal.getTime();

//		GregorianCalendar cal = new GregorianCalendar();
//		cal.setTime(myDate);
//		boolean isEndDayOfMonth_old = cal
//				.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) == cal
//				.get(GregorianCalendar.DAY_OF_MONTH);
//		cal.add(GregorianCalendar.MONTH, amount);
//		boolean isEndDayOfMonth_new = cal
//				.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) == cal
//				.get(GregorianCalendar.DAY_OF_MONTH);
//		if (isEndDayOfMonth_old && !isEndDayOfMonth_new) {
//			cal.set(GregorianCalendar.DATE, cal
//					.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
//		}
//		cal.add(GregorianCalendar.MONTH, amount);
//		return cal.getTime();
	}

	/**
	 * 为一个日期加上指定天数
	 * 
	 * @param myDate
	 * @param amount
	 *            天数
	 * @return
	 */
	public static Date addDay(Date myDate, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return cal.getTime();
	}

	/**
	 * 为一个日期加上指定小时数
	 * 
	 * @param myDate
	 * @param amount
	 *            小时数
	 * @return
	 */
	public static Date addHours(Date myDate, int amount) {
		// Date result = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.HOUR, amount);
		return cal.getTime();

	}

	/**
	 * 为一个日期加上指定分钟数
	 * 
	 * @param myDate
	 * @param amount
	 *            分钟数
	 * @return
	 */
	public static Date addMinutes(Date myDate, int amount) {
		// Date result = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.MINUTE, amount);
		return cal.getTime();

	}

	/**
	 * 为一个日期加上指定秒数
	 * 
	 * @param myDate
	 * @param amount
	 *            秒数
	 * @return
	 */
	public static Date addSeconds(Date myDate, int amount) {
		// Date result = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.SECOND, amount);
		return cal.getTime();

	}

	/**
	 * 得到当前时间的时间戳，即从1970年1月1日0时开始的毫秒数，采用中国时区
	 * 
	 * @return
	 */
	public static String getCurrentTimeStamp() {
		return Long.toString(System.currentTimeMillis()
				+ getGMT8OffSetTimeMills());
	}

	/**
	 * Returns the amount of time in milliseconds to add to UTC to get standard
	 * time in 'GMT+8' time zone. Search API for detail.
	 * 
	 * @return
	 */
	public static int getGMT8OffSetTimeMills() {

		return TimeZone.getTimeZone("GMT+8").getRawOffset();
	}
	
    /**
     * 获得当前UTC时间
     * @return
     */
    public static Date getUTCTime(){
        return getUTCTime(new Date());
    }
	
    /**
     * 获得UTC时间
     * @return
     */
    public static Date getUTCTime(Date date){
        Calendar cal = Calendar.getInstance();//当前本地时间
        cal.setTime(date);
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);//时间偏移量
        int dstOffset = cal.get(Calendar.DST_OFFSET);//夏令时差
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }
    
	/**
	 * Returns the amount of time in seconds to add to UTC to get standard time
	 * in 'GMT+8' time zone. Search API for detail.
	 * 
	 * @return
	 */
	public static int getGMT8OffSetSecends() {

		return TimeZone.getTimeZone("GMT+8").getRawOffset() / 1000;
	}

	/**
	 * 两个日期字符串相减得到的天数
	 * 
	 * @param startDateStr yyyy-MM-dd
	 * @param endDateStr yyyy-MM-dd
	 * @return
	 */
	public static int intervalDays(String startDateStr, String endDateStr)
			throws ParseException {
	    Date startDate = parseShortDateString(startDateStr);
	    Date endDate = parseShortDateString(endDateStr);
		return intervalDays(startDate, endDate);
	}

	/**
	 * 两个日期型相减得到的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int intervalDays(Date startDate, Date endDate)
			throws ParseException {
        Calendar c1 = GregorianCalendar.getInstance();
        c1.setTime(startDate);
        Calendar c2 = GregorianCalendar.getInstance();
        c2.setTime(endDate);
        int days = (int) (Math.abs(c2.getTimeInMillis() - c1.getTimeInMillis())/1000/3600/24);
		return days;
	}

	/**
	 * 计算两个年月之间的相差月数
	 * 
	 * @param strDateBegin
	 *            yyyy-mm
	 * @param strDateEnd
	 *            yyyy-mm
	 * @return
	 */
	public static int getMonthBetween(String strDateBegin, String strDateEnd) {
		try {
			int result;
			if (strDateBegin.equals("") || strDateEnd.equals("")
					|| strDateBegin.length() != 7 || strDateEnd.length() != 7) {
				result = 0;
			} else {
				int intMonthBegin = Integer.parseInt(strDateBegin.substring(0,
						4))
						* 12 + Integer.parseInt(strDateBegin.substring(5, 7));
				int intMonthEnd = Integer.parseInt(strDateEnd.substring(0, 4))
						* 12 + Integer.parseInt(strDateEnd.substring(5, 7));

				result = Math.abs(intMonthBegin - intMonthEnd);
			}
			return result;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 计算某日期所在月份天数
	 * 
	 * @param date
	 *            yyyy-mm
	 * @return
	 */
	public static int getDaysInMonth(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		days[1] += calendar.isLeapYear(calendar.get(GregorianCalendar.YEAR)) ? 1:0;
		return days[calendar.get(GregorianCalendar.MONTH)];
	}

	/**
	 * 把一个日期的字符串转成日历对象
	 * 
	 * @param strTime
	 *            日期的字符串
	 * @param iFormat
	 *            输入格式 0:yyyyMMdd 1:yyyy-MM-dd 2:yyyy-MM-dd HH:mm:ss 3:yyyy年M月d日
	 *            EEE 4:yyyy-MM-ddHHmmss
	 * 
	 * @return 日历对象
	 */

	public static Calendar getCalendar(String strTime, int iFormat) {
		Calendar cal = Calendar.getInstance();

		int iYear, iMonth, iDay, iHour, iMinute, iSecond;
		switch (iFormat) {
		case 0:
			iYear = Integer.parseInt(strTime.substring(0, 4));
			iMonth = Integer.parseInt(strTime.substring(4, 6));
			iDay = Integer.parseInt(strTime.substring(6, 8));

			cal.set(iYear, iMonth - 1, iDay);
			break;
		case 1:
			iYear = Integer.parseInt(strTime.substring(0, 4));
			iMonth = Integer.parseInt(strTime.substring(5, 7));
			iDay = Integer.parseInt(strTime.substring(8, 10));

			cal.set(iYear, iMonth - 1, iDay);
			break;
		case 2:
			iYear = Integer.parseInt(strTime.substring(0, 4));
			iMonth = Integer.parseInt(strTime.substring(5, 7));
			iDay = Integer.parseInt(strTime.substring(8, 10));
			iHour = Integer.parseInt(strTime.substring(11, 13));
			iMinute = Integer.parseInt(strTime.substring(14, 16));
			iSecond = Integer.parseInt(strTime.substring(17, 19));

			cal.set(iYear, iMonth - 1, iDay, iHour, iMinute, iSecond);
			break;
		case 4:
			iYear = Integer.parseInt(strTime.substring(0, 4));
			iMonth = Integer.parseInt(strTime.substring(5, 7));
			iDay = Integer.parseInt(strTime.substring(8, 10));
			iHour = Integer.parseInt(strTime.substring(10, 12));
			iMinute = Integer.parseInt(strTime.substring(12, 14));
			iSecond = Integer.parseInt(strTime.substring(14, 16));

			cal.set(iYear, iMonth - 1, iDay, iHour, iMinute, iSecond);
			break;

		default:
			iYear = Integer.parseInt(strTime.substring(0, 4));
			iMonth = Integer.parseInt(strTime.substring(5, 7));
			iDay = Integer.parseInt(strTime.substring(8, 10));
			cal.set(iYear, iMonth - 1, iDay);
			break;
		}

		return cal;
	}

	/**   
	 * 获得指定日期所在月份的天数
	 * 如2010年5月12日则返回数字"12"
	 */
	public static int getDayOfMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

    /**   
     * 获得指定日期所在月份
     * 如2011年10月31日则返回数字"10"
     */
    public static int getMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH)+1;
    }
	
	/**   
	 * 指定具体日期的具体时间
	 * @param date   日期
	 * @param hour   小时
	 * @param minute 分钟
	 * @param second 秒
	 */
	public static Date setTime(Date date,int hour,int minute,int second){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE,minute);
		cal.set(Calendar.SECOND, second);
		return cal.getTime();
	}
	
    /**
     * 为一个日期加上指定小时和分钟
     * 
     * @param myDate
     * @param HHmm 小时和分钟
     * @return
     */
    public static Date addHHMM(Date myDate, Date HHmm) {
        long timeStamp = myDate.getTime() + HHmm.getTime() + getGMT8OffSetTimeMills();
        return new Date(timeStamp);
    }

    /**
     * 将一个符合'yyyy年MM月dd日'格式的字串解析成日期型
     * 
     * @param strDate
     * @return
     */
    public static Date parseShortGBDateString(String strDate)
            throws ParseException {
        return parser(strDate, "yyyy年MM月dd日");
    }
    
    
    /**   
     * 获取指定日期的年份
     * @param date
     */
    public static int getYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    /**   
     * 将分钟数转换成小时：分钟，
     * @param minute 分钟数
     * @return 例如将72分钟转化为一小时12分钟，即返回 1:12
     */
    public static String toHHMIString(Integer minute){
        if(minute==null){
            return null;
        }
        int hour = minute / 60;
        minute = minute % 60;
        return hour + ":" + minute;
    }
    
    /**   
     * 返回日期的年月
     * @param aDate 日期
     * @return 返回年月格式YYYYMM
     */
    public static String toYYYYMMString(Date aDate) {
        return toFormatDateString(aDate, "yyyyMM");
    }
    
    /**   
     * 获得指定日期对应本年度第一天的日期
     * @param aDate 日期
     * @return 
     */
    public static Date getYearStartDate(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.add(Calendar.MONTH, 0);//1月
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return cal.getTime();
    }
    
    /**   
     * 获得指定日期对应本年度最后一天的日期
     * @param aDate 日期
     * @return 
     */
    public static Date getYearEndDate(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.add(Calendar.MONTH, 11);//12月
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        return cal.getTime();
    }
    
    /**   
     * 判断是否为瑞年
     * @param year 年份
     */
    public static boolean isLoopYear(int year){
        return ((year%4 ==0)&&(year%100!=0))||(year%400==0);
    }
    
    /**   
     * 获得指定月的开始日期
     * @param aDate 日期
     * @param intervalMonth 间隔月
     */
    public static Date getMonthStartDate(Date aDate,int intervalMonth){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.add(Calendar.MONTH, intervalMonth);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return setTime(cal.getTime(),0,0,0);
    }
    
    /**   
     * 获得指定月的结束日期
     * @param aDate 日期
     * @param intervalMonth 间隔月
     */
    public static Date getMonthEndDate(Date aDate,int intervalMonth){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.add(Calendar.MONTH, intervalMonth);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return setTime(cal.getTime(),23,59,59);
    }
    
    /**   
     * 获得指定月的结束日期
     * @param year 年
     * @param month 月(1~12)
     */
    public static Date getMonthEndDate(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return setTime(cal.getTime(),23,59,59);
    }
    
    /**   
     * 获得指定月的开始日期
     * @param year 年
     * @param month 月(1~12)
     */
    public static Date getMonthStartDate(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return setTime(cal.getTime(),0,0,0);
    }
    
    /**   
     * 获取指定日期对应的星期数字,周日为7
     * @param date 日期
     */
    public static int getDayForWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }
    
    /**   
     * 通过生日计算年龄
     * @param birthday 日期
     */
    public static String getAge(Date birthday){
        if(birthday == null){
            return "";
        }
        return DurationFormatUtils.formatPeriod(birthday.getTime(), Calendar.getInstance().getTimeInMillis(), "y");
    }
    
    /**   
     * 获取指定日期所在周星期一的日期
     * @param date 日期
     */
    public static Date getMondayDate(Date date){
        setTime(date,0,0,0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return setTime(cal.getTime(),0,0,0);
    }
    
    /**   
     * 获取指定日期所在周星期天的日期
     * @param date 日期
     */
    public static Date getSundayDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, +1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return setTime(cal.getTime(),0,0,0);
    }
}
