package com.dszj.manage.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http请求工具封装
 * 
 * @author yys
 *
 */
@Slf4j
public class HttpClientUtil {

	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);

			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			log.error("HttpClientUtil.doGet请求异常",e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				log.error("HttpClientUtil.doGet--连接关闭异常",e);
			}
		}
		return resultString;
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			log.error("HttpClientUtil.doPost请求异常",e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException e) {
				log.error("HttpClientUtil.doPost--连接关闭异常",e);
			}
		}

		return resultString;
	}

	@SuppressWarnings("deprecation")
	public static String doPostByBody(String url, String requestBody) {
		String responseBody = null;
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			post.setRequestBody(requestBody);
			httpclient.executeMethod(post);
			responseBody = new String(post.getResponseBody(), "utf-8");
		} catch (Exception e) {
			log.error("HttpClientUtil.doPostByBody请求异常",e);
		}

		return responseBody;
	}

	public static String doPostXml(String url, String xml) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(xml, "UTF-8"));
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			log.error("HttpClientUtil.doPostXml请求异常",e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException e) {
				log.error("HttpClientUtil.doPostXml--连接关闭异常",e);
			}
		}

		return resultString;
	}

	/**
	 * 通过证书发起post请求
	 * @param url
	 * @param xml 请求数据
	 * @param certPath 证书路径
	 * @param certPassword 证书密码  微信默认等于商户id
	 * @return
	 * @author yys
	 */
	public static String doPostXmlByCert(String url, String xml, String certPath, String certPassword) {

		// 创建Httpclient对象
		CloseableHttpResponse response = null;
		String resultString = "";
		InputStream inputStream = null;
		CloseableHttpClient httpClient = null;
		try {

			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			// 指向你的证书的绝对路径，带着证书去访问
			inputStream = new PathMatchingResourcePatternResolver().getResource(certPath).getInputStream();
			keyStore.load(inputStream, certPassword.toCharArray());

//			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();

//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
//					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//			
//			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			
			
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
					NoopHostnameVerifier.INSTANCE);
			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(xml, "UTF-8"));
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			log.error("HttpClientUtil.doPostXmlByCert请求异常",e);
		} finally {
			try {
				if (response!=null) {
					response.close();
				}
				if (inputStream!=null) {
					inputStream.close();
				}
				if (httpClient!=null) {
					httpClient.close();
				}
				
			} catch (IOException e) {
				log.error("HttpClientUtil.doPostXmlByCert--连接关闭异常",e);

			}
		}

		return resultString;
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}

	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resultString;
	}
}