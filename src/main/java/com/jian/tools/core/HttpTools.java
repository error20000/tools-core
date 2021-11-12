package com.jian.tools.core;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpTools {
	
	private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(10 * 1000)
            .setConnectTimeout(10 * 1000)
            .setConnectionRequestTimeout(10 * 1000)
            .build();
	
	private static HttpTools instance = null;
	
	private HttpTools(){
		
	}
	
	public static HttpTools getInstance(){
		if (instance == null) {
			instance = new HttpTools();
		}
		return instance;
	}
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @throws Exception 
	 */
	public String sendHttpPost(String httpUrl) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param params 参数(格式:key1=value1&key2=value2)
	 * @throws Exception 
	 */
	public String sendHttpPost(String httpUrl, String params) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		try {
			//设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			throw e;
		}
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param params 参数
	 * @param type 参数格式
	 * @throws Exception 
	 */
	public String sendHttpPost(String httpUrl, String params, ContentType type) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		try {
			//设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType(type.getMimeType());
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			throw e;
		}
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param maps 参数
	 * @throws Exception 
	 */
	public String sendHttpPost(String httpUrl, Map<String, Object> maps) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		// 创建参数队列  
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)+""));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			throw e;
		}
		return sendHttpPost(httpPost);
	}
	
	
	/**
	 * 发送 post请求（带文件）
	 * @param httpUrl 地址
	 * @param maps 参数
	 * @param fileLists 附件
	 * @throws Exception 
	 */
	public String sendHttpPost(String httpUrl, Map<String, Object> maps, List<File> fileLists) throws Exception {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		for (String key : maps.keySet()) {
			meBuilder.addPart(key, new StringBody(maps.get(key)+"", ContentType.TEXT_PLAIN));
		}
		for(File file : fileLists) {
			FileBody fileBody = new FileBody(file);
			meBuilder.addPart("files", fileBody);
		}
		HttpEntity reqEntity = meBuilder.build();
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送Post请求
	 * @param httpPost
	 * @return
	 * @throws Exception 
	 */
	public String sendHttpPost(HttpPost httpPost) throws Exception {
		return sendHttpPost(httpPost, requestConfig);
	}
	
	/**
	 * 发送Post请求
	 * @param httpPost
	 * @param requestConfig
	 * @return
	 * @throws Exception 
	 */
	public String sendHttpPost(HttpPost httpPost, RequestConfig requestConfig) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				response = null;
				httpClient = null;
			}
		}
		return responseContent;
	}
	

	/**
	 * 发送 get请求
	 * @param httpUrl
	 * @throws Exception 
	 */
	public String sendHttpGet(String httpUrl) throws Exception {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpGet(httpGet);
	}
	
	/**
	 * 发送 get请求Https
	 * @param httpUrl
	 * @throws Exception 
	 */
	public String sendHttpsGet(String httpUrl) throws Exception {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpsGet(httpGet);
	}
	
	/**
	 * 发送Get请求
	 * @param httpGet
	 * @return
	 * @throws Exception 
	 */
	public String sendHttpGet(HttpGet httpGet) throws Exception {
		return  sendHttpGet(httpGet, requestConfig);
	}
	
	/**
	 * 发送Get请求
	 * @param httpGet
	 * @param requestConfig
	 * @return
	 * @throws Exception 
	 */
	public String sendHttpGet(HttpGet httpGet, RequestConfig requestConfig) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				response = null;
				httpClient = null;
			}
		}
		return responseContent;
	}
	
	/**
	 * 发送Get请求Https
	 * @param httpPost
	 * @return
	 * @throws Exception 
	 */
	public String sendHttpsGet(HttpGet httpGet) throws Exception {
		return sendHttpsGet(httpGet, requestConfig);
	}
	
	/**
	 * 发送Get请求Https
	 * @param httpPost
	 * @return
	 * @throws Exception 
	 */
	public String sendHttpsGet(HttpGet httpGet, RequestConfig requestConfig) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				response = null;
				httpClient = null;
			}
		}
		return responseContent;
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://s9.gw.gf.ppgame.com/index.php/1009/Api/getRole";

		Map<String, Object> map = new HashMap<>();
		map.put("openId", "4378142930864547");
		long time = System.currentTimeMillis()/1000;
		map.put("time", time+"");
		map.put("sign", Tools.md5("openId=4378142930864547&time="+time));
		
		String data = "openId=4378142930864547&time="+time+"&sign="+Tools.md5("openId=4378142930864547&time="+time);
		
		String str1 = HttpTools.getInstance().sendHttpPost(url, map);
		System.out.println(str1);
		String str2 = HttpTools.getInstance().sendHttpPost(url, data);
		System.out.println(str2);
		
	}
	
	
}

