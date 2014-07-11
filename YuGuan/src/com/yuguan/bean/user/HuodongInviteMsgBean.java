package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class HuodongInviteMsgBean implements Serializable {
	private static final long serialVersionUID = 2349871707461604991L;
	private long id;
	private long uid;
	private int aid;
	private String aname;
	private String uname;
	private String ftime;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return this.uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getAid() {
		return this.aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getAname() {
		return this.aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getUname() {
		return this.uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getFtime() {
		return this.ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	
	public static HuodongInviteMsgBean getBeanByJson(JSONObject json){
		try {
			HuodongInviteMsgBean bean = new HuodongInviteMsgBean();
			bean.setAid(json.getInt("aid"));
			bean.setAname(json.getString("aname"));
			bean.setFtime(json.getString("ftime"));
			bean.setId(json.getLong("id"));
			bean.setUid(json.getLong("uid"));
			bean.setUname(json.getString("uname"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.HuodongInviteMsgBean JD-Core Version: 0.6.0
 */