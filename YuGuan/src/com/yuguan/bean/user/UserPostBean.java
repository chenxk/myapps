package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class UserPostBean implements Serializable {
	private static final long serialVersionUID = -1377171761210935407L;
	private long id;
	private long tid;
	private String subject;
	private String ftime;
	private String text;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTid() {
		return this.tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFtime() {
		return this.ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public static UserPostBean getBeanByJson(JSONObject json){
		try {
			UserPostBean bean = new UserPostBean();
			bean.setId(json.getLong("id"));
			bean.setSubject(json.getString("subject"));
			bean.setText(json.getString("text"));
			bean.setTid(json.getLong("tid"));
			bean.setFtime(json.getString("ftime"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.UserPostBean JD-Core Version: 0.6.0
 */