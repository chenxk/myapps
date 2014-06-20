package com.yuguan.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpPostUtil implements Runnable {

	// ����һ��HttpClient����
	private HttpClient httpclient;
	private String processURL;
	private Handler handler;
	private String key;
	private int TIME_OUT_DELAY = 3000;

	private String result;
	private List<NameValuePair> pairs;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	
	
	public List<NameValuePair> getPairs() {
		return pairs;
	}

	public void setPairs(List<NameValuePair> pairs) {
		this.pairs = pairs;
	}

	public HttpPostUtil() {
		httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				TIME_OUT_DELAY); // ��ʱ����
		httpclient.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, TIME_OUT_DELAY);// ���ӳ�ʱ
	}

	public HttpPostUtil(String processURL) {
		this();
		this.processURL = processURL;
	}

	public HttpPostUtil(String processURL, Handler handler, String key) {
		this(processURL);
		this.handler = handler;
		this.key = key;
	}
	

	public String getJsonByProcessURL() throws ClientProtocolException,
			IOException {
		// ����HttpGet����
		HttpPost post = new HttpPost(processURL);
		
		HttpEntity entity = new UrlEncodedFormEntity(pairs,HTTP.UTF_8);//���ñ��룬��ֹ��������
		
		post.setEntity(entity);   
		
		 //HttpResponse response = httpclient.execute(post);

		//����˷��ص�����

		//String data = EntityUtils.toString(response.getEntity());
		// ������Ϣ����MIMEÿ����Ӧ���͵��������ͨ�ı���html �� XML��json�����������Ӧ����Ӧ��ƥ����Դ�������ɵ� MIME ����
		// ��Դ�����ɵ� MIME ����Ӧ��ƥ��һ�ֿɽ��ܵ� MIME ���͡�������ɵ� MIME ���ͺͿɽ��ܵ� MIME ���Ͳ� ƥ�䣬��ô��
		// ���� com.sun.jersey.api.client.UniformInterfaceException�����磬���ɽ��ܵ� MIME
		// ��������Ϊ text/xml������
		// ���ɵ� MIME ��������Ϊ application/xml�������� UniformInterfaceException��
		post.addHeader("Accept", "text/json");
		// ��ȡ��Ӧ�Ľ��
		HttpResponse response = httpclient.execute(post);
		// ��ȡHttpEntity
		HttpEntity resentity = response.getEntity();
		// ��ȡ��Ӧ�Ľ����Ϣ
		result = EntityUtils.toString(resentity, "UTF-8");
		
		if(result == null)
			result = "�������ʧ��";
		
		// �ر�����
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
			result = "�������ʧ��";
		} catch (IOException e) {
			result = "�������ʧ��";
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
