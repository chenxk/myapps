package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class UserPrvmsgBean implements Serializable {
	private static final long serialVersionUID = 993254483113013541L;
	private long id;
	private long uid;
	private String uname;
	private String upic;
	private String text;
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

	public String getUname() {
		return this.uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUpic() {
		return this.upic;
	}

	public void setUpic(String upic) {
		this.upic = upic;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFtime() {
		return this.ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	
	public static UserPrvmsgBean getBeanByJson(JSONObject json){
		try {
			UserPrvmsgBean bean = new UserPrvmsgBean();
			bean.setId(json.getLong("id"));
			bean.setUid(json.getLong("uid"));
			bean.setUname(json.getString("uname"));
			bean.setUpic(json.getString("upic"));
			bean.setText(json.getString("text"));
			bean.setFtime(json.getString("ftime"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.UserPrvmsgBean JD-Core Version: 0.6.0
 */