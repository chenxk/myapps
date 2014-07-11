package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class UserActActionBean implements Serializable {
	private static final long serialVersionUID = -8655358839195357333L;
	private int aid;
	private String aname;
	private String pic;
	private String ftime;
	private int status;
	private int sort;
	private int score;
	private int reviewed;

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

	public int getSort() {
		return this.sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getReviewed() {
		return this.reviewed;
	}

	public void setReviewed(int reviewed) {
		this.reviewed = reviewed;
	}
	
	public static UserActActionBean getBeanByJson(JSONObject json){
		try {
			UserActActionBean bean = new UserActActionBean();
			bean.setAid(json.getInt("aid"));
			bean.setAname(json.getString("aname"));
			bean.setFtime(json.getString("ftime"));
			bean.setPic(json.getString("pic"));
			bean.setReviewed(json.getInt("reviewed"));
			bean.setScore(json.getInt("score"));
			bean.setSort(json.getInt("scort"));
			bean.setStatus(json.getInt("status"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.UserActActionBean JD-Core Version: 0.6.0
 */