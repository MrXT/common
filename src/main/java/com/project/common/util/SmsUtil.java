
package com.project.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;



/** 
 * 说明：短信接口类
 * 创建人：FH Q313596790
 * 修改时间：2013年2月22日
 * @version
 */
public class SmsUtil {
    
//    public static void main(String [] args) {
//        
//        sendSms1("18200390083","您的验证码是：1111。请不要把验证码泄露给其他人。");
//        //sendSmsAll(List<PageData> list)
//        
//        //sendSms1();
//    }

     //短信商 一  http://www.dxton.com/ =====================================================================================
    /**
     * 给一个人发送单条短信
     * @param mobile 手机号
     * @param code  短信内容
     */
    public static void sendSms1(String mobile,String code){
        String account = "qiyiwl_fayuan", password = "Qiyiwl_fayuan";
        String PostData = "";
        try {
            PostData = "account="+account+"&password="+password+"&mobile="+mobile+"&content="+URLEncoder.encode(code,"utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("短信提交失败");
        }
         //System.out.println(PostData);
         String ret = SMS(PostData, "http://sms.106jiekou.com/utf8/sms.aspx");
         System.out.println(ret);
       /*  
       100          发送成功
       101          验证失败
       102          手机号码格式不正确
       103          会员级别不够
       104          内容未审核
       105          内容过多
       106          账户余额不足
       107          Ip受限
       108          手机号码发送太频繁，请换号或隔天再发
       109          帐号被锁定
       110          发送通道不正确
       111          当前时间段禁止短信发送
       120          系统升级
        */
         
    }
    
     public static String SMS(String postData, String postUrl) {
            try {
                //发送POST请求
                URL url = new URL(postUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length", "" + postData.length());
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                out.write(postData);
                out.flush();
                out.close();
                //获取响应状态
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    System.out.println("connect failed!");
                    return "";
                }
                //获取响应内容体
                String line, result = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                while ((line = in.readLine()) != null) {
                    result += line + "\n";
                }
                in.close();
                return result;
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
            return "";
        }
     /**
      * 
      * @param url 应用地址，类似于http://ip:port/msg/
      * @param account 账号
      * @param pswd 密码
      * @param mobile 手机号码，多个号码使用","分割
      * @param msg 短信内容
      * @param needstatus 是否需要状态报告，需要true，不需要false
      * @return 返回值定义参见HTTP协议文档
      * @throws Exception
      */
     public static String send(String url, String account, String pswd, String mobile, String msg,
             boolean needstatus, String product, String extno) throws Exception {
         HttpClient client = new HttpClient();
         GetMethod method = new GetMethod();
         try {
             URI base = new URI(url, false);
             method.setURI(new URI(base, "HttpSendSM", false));
             method.setQueryString(new NameValuePair[] { 
                     new NameValuePair("account", account),
                     new NameValuePair("pswd", pswd), 
                     new NameValuePair("mobile", mobile),
                     new NameValuePair("needstatus", String.valueOf(needstatus)), 
                     new NameValuePair("msg", msg),
                     new NameValuePair("product", product), 
                     new NameValuePair("extno", extno), 
                 });
             int result = client.executeMethod(method);
             if (result == HttpStatus.SC_OK) {
                 InputStream in = method.getResponseBodyAsStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 byte[] buffer = new byte[1024];
                 int len = 0;
                 while ((len = in.read(buffer)) != -1) {
                     baos.write(buffer, 0, len);
                 }
                 return URLDecoder.decode(baos.toString(), "UTF-8");
             } else {
                 throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
             }
         } finally {
             method.releaseConnection();
         }

     }
     /**
      * 
      * @param url 应用地址，类似于http://ip:port/msg/
      * @param account 账号
      * @param pswd 密码
      * @param mobile 手机号码，多个号码使用","分割
      * @param msg 短信内容
      * @param needstatus 是否需要状态报告，需要true，不需要false
      * @return 返回值定义参见HTTP协议文档
      * @throws Exception
      */
     public static String batchSend(String url, String account, String pswd, String mobile, String msg,
             boolean needstatus, String product, String extno) throws Exception {
         HttpClient client = new HttpClient();
         GetMethod method = new GetMethod();
         try {
             URI base = new URI(url, false);
             method.setURI(new URI(base, "HttpBatchSendSM", false));
             method.setQueryString(new NameValuePair[] { 
                     new NameValuePair("account", account),
                     new NameValuePair("pswd", pswd), 
                     new NameValuePair("mobile", mobile),
                     new NameValuePair("needstatus", String.valueOf(needstatus)), 
                     new NameValuePair("msg", msg),
                     new NameValuePair("product", product),
                     new NameValuePair("extno", extno), 
                 });
             int result = client.executeMethod(method);
             if (result == HttpStatus.SC_OK) {
                 InputStream in = method.getResponseBodyAsStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 byte[] buffer = new byte[1024];
                 int len = 0;
                 while ((len = in.read(buffer)) != -1) {
                     baos.write(buffer, 0, len);
                 }
                 return URLDecoder.decode(baos.toString(), "UTF-8");
             } else {
                 throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
             }
         } finally {
             method.releaseConnection();
         }

     }
    
}


