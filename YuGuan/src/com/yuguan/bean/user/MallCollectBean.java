package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class MallCollectBean implements Serializable {
	private static final long serialVersionUID = -9006657237951891397L;
	private int id;
	private String name;
	private String desc;
	private String addr;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	public static MallCollectBean getBeanByJson(JSONObject json){
		try {
			MallCollectBean bean = new MallCollectBean();
			bean.setAddr(json.getString("addr"));
			bean.setDesc(json.getString("desc"));
			bean.setId(json.getInt("id"));
			bean.setName(json.getString("name"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.MallCollectBean JD-Core Version: 0.6.0
 */