package com.yuguan.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpUtil implements Runnable {

	// 创建一个HttpClient对象
	private HttpClient httpclient;
	private String processURL;
	private Handler handler;
	private String key;
	private int TIME_OUT_DELAY = 3000;

	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public HttpUtil() {
		httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				TIME_OUT_DELAY); // 超时设置
		httpclient.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, TIME_OUT_DELAY);// 连接超时
	}

	public HttpUtil(String processURL) {
		this();
		this.processURL = processURL;
	}

	public HttpUtil(String processURL, Handler handler, String key) {
		this(processURL);
		this.handler = handler;
		this.key = key;
	}

	public String getJsonByProcessURL() throws ClientProtocolException,
			IOException {
		// 创建HttpGet对象
		HttpGet request = new HttpGet(processURL);
		// 请求信息类型MIME每种响应类型的输出（普通文本、html 和 XML，json）。允许的响应类型应当匹配资源类中生成的 MIME 类型
		// 资源类生成的 MIME 类型应当匹配一种可接受的 MIME 类型。如果生成的 MIME 类型和可接受的 MIME 类型不 匹配，那么将
		// 生成 com.sun.jersey.api.client.UniformInterfaceException。例如，将可接受的 MIME
		// 类型设置为 text/xml，而将
		// 生成的 MIME 类型设置为 application/xml。将生成 UniformInterfaceException。
		request.addHeader("Accept", "text/json");
		// 获取响应的结果
		HttpResponse response = httpclient.execute(request);
		// 获取HttpEntity
		HttpEntity entity = response.getEntity();
		// 获取响应的结果信息
		result = EntityUtils.toString(entity, "UTF-8");
		
		if(result == null)
			result = "服务访问失败";
		// 关闭连接
		httpclient.getConnectionManager().shutdown();

		
		return result;
	}

	@Override
	public void run() {
		String result = null;
		try {
			result = getJsonByProcessURL();
			if (result == null) {
				result = "";
			}
		} catch (ClientProtocolException e) {
			result = "服务访问失败";
		} catch (IOException e) {
			result = "服务访问失败";
		}

		this.setResult(result);
		
		if (handler != null) {
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString(key, result);
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}

}
