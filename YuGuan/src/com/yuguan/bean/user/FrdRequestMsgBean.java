package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class FrdRequestMsgBean implements Serializable {
	private static final long serialVersionUID = 3531197397887475532L;
	private long id;
	private long uid;
	private String uname;
	private String upic;
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

	public String getFtime() {
		return this.ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	
	public static FrdRequestMsgBean getBeanByJson(JSONObject json){
		try {
			FrdRequestMsgBean bean = new FrdRequestMsgBean();
			bean.setFtime(json.getString("ftime"));
			bean.setId(json.getLong("id"));
			bean.setUid(json.getLong("uid"));
			bean.setUname(json.getString("uname"));
			bean.setUpic(json.getString("upic"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

