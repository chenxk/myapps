package com.yuguan.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class FriendMsgBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String pic;
	private String msg;
	private String time;
	private int uid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public static FriendMsgBean getBeanFromJson(JSONObject json) {
		FriendMsgBean bean = new FriendMsgBean();
		try {
			bean.setName(json.getString("name"));
			bean.setPic(json.getString("pic"));
			bean.setUid(json.getInt("uid"));
			bean.setMsg(json.getString(""));
			bean.setTime(json.getString(""));
			return bean;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}
