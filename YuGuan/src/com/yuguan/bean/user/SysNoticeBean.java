package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class SysNoticeBean implements Serializable {
	private static final long serialVersionUID = 328234371304421089L;
	private long id;
	private String msg;
	private String ftime;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFtime() {
		return this.ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	
	public static SysNoticeBean getBeanByJson(JSONObject json){
		try {
			SysNoticeBean bean = new SysNoticeBean();
			bean.setFtime(json.getString("ftime"));
			bean.setMsg(json.getString("msg"));
			bean.setId(json.getInt("id"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.SysNoticeBean JD-Core Version: 0.6.0
 */