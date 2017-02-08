package com.project.common.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
 
/**
 * Copyright (c) 2015, 北京卡拉卡尔科技股份有限公司 All rights reserved.
 *
 * @Description: Http请求文本类型工具类，获取返回的字符串结果
 * @since
 * @author xiaolong.li@karakal.com.cn (李小龙)   
 * @date 2015年7月21日 上午11:03:31 
 * @version 1.0
 */
public class HttpClientUtil {
	
	public static RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(15000).build();
	
	/**
	 * @Description: get方式获取文本信息 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception    
	 * @throws 
	 * @author xiaolong.li@karakal.com.cn (李小龙)   
	 * @date 2015年7月21日 上午11:04:11 
	 * @version 1.0
	 * @throws Exception 
	 */
	private static String get(String url, Map<String, String> params, Map<String, String> headers, RequestConfig requestConfig) throws URISyntaxException, IOException, Exception, Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(createURI(url, params));
		get.setConfig(requestConfig);
		addHeaders(get, headers);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(get);
			StatusLine sl = response.getStatusLine();
			if(sl.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return toString(entity, "UTF-8");
				} else {
					throw new Exception("response entity is null error, request URL[" + url + "]");
				}
			} else {
				throw new Exception("response status error：" + sl + ", request URL[" + url + "]");
			}
		} finally {
			IOUtils.closeQuietly(response);
		}
		
	}	
	
	
	
	/**
	 * 
	 * 功能:get 请求. <br/>
	 * @param url 地址
	 * @param params 参数
	 * @param headers 头参数
	 * @param recount 尝试次数
	 * @return
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers, int recount){
		for (int i = 0; i < recount; ++i) {
			try {
				return get(url, params, headers, defaultRequestConfig);
			} catch (Exception e) {
				LogUtils.ERROR(e.getMessage());
			}
		}
		return "";
	}
	/**
	 * @Description: post方式获取文本信息 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception    
	 * @throws 
	 * @author xiaolong.li@karakal.com.cn (李小龙)   
	 * @date 2015年7月21日 上午11:04:11 
	 * @version 1.0
	 * @throws Exception 
	 */
	private static String post(String url, Map<String, String> params, Map<String, String> headers, RequestConfig requestConfig) throws URISyntaxException, IOException, Exception, Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		addHeaders(post, headers);
		CloseableHttpResponse response = null;
		try {
			addPostParams(post, params);
			response = httpClient.execute(post);
			StatusLine sl = response.getStatusLine();
			if(sl.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return toString(entity, "UTF-8");
				} else {
					throw new Exception("response entity is null error, request URL[" + url + "]");
				}
			} else {
				throw new Exception("response status error：" + sl + ", request URL[" + url + "]");
			}
		} finally {
			IOUtils.closeQuietly(response);
		}
	}
	
	/**
	 * post请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param headers 请求头
	 * @param recount 尝试次数
	 * @return
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> headers, int recount){
		for (int i = 0; i < recount; ++i) {
			try {
				return post(url, params, headers, defaultRequestConfig);
			} catch (Exception e) {
			    LogUtils.ERROR(e.getMessage());
			} 
		}
		return "";
	}
	
	/**
	 * @Description: 添加Post参数
	 * @param post
	 * @param params
	 * @throws UnsupportedEncodingException    
	 * @throws 
	 * @author xiaolong.li@karakal.com.cn (李小龙)   
	 * @date 2015年7月22日 上午11:18:30 
	 * @version 1.0
	 */
	public static void addPostParams(HttpPost post, Map<String, String> params) throws UnsupportedEncodingException {
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		if (params != null) {
			for (String key : params.keySet()) {
				formParams.add(new BasicNameValuePair(key, params.get(key)
						.toString()));
			}
		}
		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
		post.setEntity(uefEntity);
	}
	
	/**
	 * @Description: 添加头信息 
	 * @param request
	 * @param headers    
	 * @throws 
	 * @author xiaolong.li@karakal.com.cn (李小龙)   
	 * @date 2015年7月21日 上午11:04:56 
	 * @version 1.0
	 */
	public static void addHeaders(HttpRequestBase request, Map<String, String> headers) {
		if (headers != null) {
			for (String key : headers.keySet()) {
				request.addHeader(key, headers.get(key));
			}
		}
 	}
	
	/**
	 * @Description: 创建URI 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException    
	 * @throws 
	 * @author xiaolong.li@karakal.com.cn (李小龙)   
	 * @date 2015年7月21日 上午11:05:12 
	 * @version 1.0
	 */
	public static URI createURI(String url, Map<String, String> params) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(url);
		if (params != null) {
			for(String key : params.keySet()) {
				builder.addParameter(key, params.get(key));
			}
		}
		return builder.build();
	}
	
	/**
	 * @Description: 获取实体中的字符串
	 * @param entity
	 * @param defaultCharset
	 * @return
	 * @throws IOException
	 * @throws ParseException    
	 * @throws 
	 * @author xiaolong.li@karakal.com.cn (李小龙)   
	 * @date 2015年10月15日 下午5:05:57 
	 * @version 1.0
	 */
	public static String toString(
            final HttpEntity entity, final String defCharset) throws IOException, ParseException {
		Charset defaultCharset = defCharset != null ? Charset.forName(defCharset) : null;
        Args.notNull(entity, "Entity");
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                    "HTTP entity too large to be buffered in memory");
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = null;
            try {
                final ContentType contentType = ContentType.get(entity);
                if (contentType != null) {
                    charset = contentType.getCharset();
                }
            } catch (final UnsupportedCharsetException ex) {
                if (defaultCharset == null) {
                    throw new UnsupportedEncodingException(ex.getMessage());
                }
            }
            if (charset == null) {
                charset = defaultCharset;
            } else {
            	if (charset.equals(Charset.forName("GB2312"))) {
                	charset = Charset.forName("GBK");
                }
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            final Reader reader = new InputStreamReader(instream, charset);
            final CharArrayBuffer buffer = new CharArrayBuffer(i);
            final char[] tmp = new char[1024];
            int l;
            while((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toString();
        } finally {
            instream.close();
        }
    }
	
}
