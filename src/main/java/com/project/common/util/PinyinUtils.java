package com.project.common.util;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 中文转化成拼音工具类
 * @author WJK
 * @version 2012-10
 */
public class PinyinUtils {
    public static final String DEFAULT_STRING_SEPARATOR = ",";

    /**
     * 获取中文汉字拼音
     * @param chinese 中文汉字
     * @return 如果汉字为多音字则以逗号分隔
     */
    public static String getPinyin(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese));
    }

    /**
     * 拼音大写输出
     * @param chinese 中文汉字
     * @return 如果汉字为多音字则以逗号分隔
     */
    public static String getPinyinToUpperCase(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese)).toUpperCase();
    }

    /**
     * 拼音小写输出
     * @param chinese 中文汉字
     * @return 如果汉字为多音字则以逗号分隔
     */
    public static String getPinyinToLowerCase(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese)).toLowerCase();
    }

    /**
     * 首字母大写输出
     * @param chinese 中文汉字
     * @return 如果汉字为多音字则以逗号分隔
     */
    public static String getPinyinFirstToUpperCase(String chinese) {
        return getPinyin(chinese);
    }

    /**
     * 拼音简拼输出
     * @param chinese 中文汉字
     * @return 如果汉字为多音字则以逗号分隔
     */
    public static String getPinyinJianPin(String chinese) {
        return getPinyinConvertJianPin(getPinyin(chinese));
    }

    /**
     * 字符集转换
     * @param chinese 中文汉字
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static Set<String> makeStringByStringSet(String chinese) {
        if(chinese == null){
            return null;
        }
        char[] chars = chinese.toCharArray();
        if (chinese != null && !chinese.trim().equalsIgnoreCase("")) {
            char[] srcChar = chinese.toCharArray();
            String[][] temp = new String[chinese.length()][];
            for (int i = 0; i < srcChar.length; i++) {
                char c = srcChar[i];

                // 中文转换拼音
                try {
                    temp[i] = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());
                    if(temp[i]==null){
                        temp[i] = new String[] { String.valueOf(c) };//非中文原样输出
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }
            String[] pingyinArray = convertToArray(temp);
            Set<String> zhongWenPinYin = new HashSet<String>();
            for (int i = 0; i < pingyinArray.length; i++) {
                zhongWenPinYin.add(pingyinArray[i]);
            }
            return zhongWenPinYin;
        }
        return null;
    }

    /**
     * Default Format 默认输出格式
     * @return
     */
    public static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示
        return format;
    }

    /**
     * 如果单词有多种读音，则返回每种读音组成的拼音字符串数组
     * @param strJaggedArray 原始二维数组，一维汉字、二维多有读音
     * @return
     */
    public static String[] convertToArray(String[][] strJaggedArray) {
        String[][] temp = doExchange(strJaggedArray);
        return temp[0];
    }

    /**
     * 通过递归算法将二维数组中的数据合并，最终返回的数据全部集中在数组的起始位置
     * @param strJaggedArray 未处理完的数组
     * @return 
     */
    private static String[][] doExchange(String[][] strJaggedArray) {
        int len = strJaggedArray.length;
        if (len >= 2) {
            int len1 = strJaggedArray[0].length;
            int len2 = strJaggedArray[1].length;
            int newlen = len1 * len2;
            String[] temp = new String[newlen];
            int index = 0;
            for (int i = 0; i < len1; i++) {
                for (int j = 0; j < len2; j++) {
                    temp[index] = capitalize(strJaggedArray[0][i]) + capitalize(strJaggedArray[1][j]);
                    index++;
                }
            }
            String[][] newArray = new String[len - 1][];
            for (int i = 2; i < len; i++) {
                newArray[i - 1] = strJaggedArray[i];
            }
            newArray[0] = temp;
            return doExchange(newArray);
        } else {
            return strJaggedArray;
        }
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        char ch[];
        ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 字符串集合转换字符串
     * @param stringSet
     * @return
     */
    public static String getPinyinZh_CN(Set<String> stringSet) {
        return getPinyinZh_CN(stringSet, DEFAULT_STRING_SEPARATOR);
    }
    
    /**
     * 字符串集合转换字符串
     * @param stringSet
     * @param separator 分隔符
     * @return
     */
    public static String getPinyinZh_CN(Set<String> stringSet, String separator) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (String s : stringSet) {
            if (i == stringSet.size() - 1) {
                str.append(s);
            } else {
                str.append(s + DEFAULT_STRING_SEPARATOR);
            }
            i++;
        }
        return str.toString();
    }

    /**
     * 获取中文字符的简拼
     * @param chinese 中文字符
     * @return
     */
    public static String getPinyinConvertJianPin(String chinese) {
        return getPinyinConvertJianPin(chinese, DEFAULT_STRING_SEPARATOR);
    }
    
    /**
     * 获取中文字符的简拼
     * @param chinese 中文字符
     * @param separator 分隔符
     * @return
     */
    public static String getPinyinConvertJianPin(String chinese, String separator) {
        String[] strArray = chinese.split(",");
        String strChar = "";
        for (String str : strArray) {
            strChar += DEFAULT_STRING_SEPARATOR;
            char arr[] = str.toCharArray(); // 将字符串转化成char型数组
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] >= 65 && arr[i] < 91) { // 判断是否是大写字母
                    strChar += new String(arr[i] + "");
                }
            }
        }
        return distinct(strChar.substring(1));
    }
    
    /**
     * 去除重复数据
     * @param str
     * @return
     */
    private static String distinct(String str) {
        Set<String> set = new HashSet<String>();
        String result = "";
        
        String[] strArray = str.split(DEFAULT_STRING_SEPARATOR);
        for (String word : strArray) {
            set.add(word);
        }
        for(String word : set){
            result += DEFAULT_STRING_SEPARATOR + word;  
        }
        return result.substring(1);
    }

    public static void main(String[] args) {
        String str = "吴界鲲";
        System.out.println("默认输出：" + getPinyin(str));
        System.out.println("小写输出：" + getPinyinToLowerCase(str));
        System.out.println("大写输出：" + getPinyinToUpperCase(str));
        System.out.println("首字母大写输出：" + getPinyinFirstToUpperCase(str));
        System.out.println("简拼输出：" + getPinyinJianPin(str));
    }
}
