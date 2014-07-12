package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class UserOrgActionBean implements Serializable {
	private static final long serialVersionUID = -5532749377891261130L;
	private int aid;
	private String aname;
	private String pic;
	private String ftime;
	private int status;
	private int score;
	private int usernum;

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

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getFtime() {
		return this.ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getUsernum() {
		return this.usernum;
	}

	public void setUsernum(int usernum) {
		this.usernum = usernum;
	}
	
	public static UserOrgActionBean getBeanByJson(JSONObject json){
		try {
			UserOrgActionBean bean = new UserOrgActionBean();
			bean.setAid(json.getInt("aid"));
			bean.setAname(json.getString("aname"));
			bean.setFtime(json.getString("ftime"));
			bean.setPic(json.getString("pic"));
			bean.setScore(json.getInt("score"));
			bean.setStatus(json.getInt("status"));
			bean.setUsernum(json.getInt("usernum"));
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.UserOrgActionBean JD-Core Version: 0.6.0
 */