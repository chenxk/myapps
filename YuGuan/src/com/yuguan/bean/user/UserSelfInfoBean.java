package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

public class UserSelfInfoBean implements Serializable {
	private static final long serialVersionUID = -9026985994883244325L;
	private String name;
	private String pic;
	private int sex;
	private String birthday;
	private int flood;
	private int pid;
	private int cid;
	private int rid;
	private String province;
	private String city;
	private String region;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getSex() {
		return this.sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getFlood() {
		return this.flood;
	}

	public void setFlood(int flood) {
		this.flood = flood;
	}

	public int getPid() {
		return this.pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getCid() {
		return this.cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getRid() {
		return this.rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	public static UserSelfInfoBean getBeanByJson(JSONObject json){
		try {
			UserSelfInfoBean bean = new UserSelfInfoBean();
			bean.setBirthday(json.getString("birthday"));
			bean.setCid(json.getInt("cid"));
			bean.setCity(json.getString("city"));
			bean.setFlood(json.getInt("flood"));
			bean.setName(json.getString("name"));
			bean.setPic(json.getString("pic"));
			bean.setPid(json.getInt("pid"));
			bean.setProvince(json.getString("province"));
			bean.setRegion(json.getString("region"));
			bean.setRid(json.getInt("rid"));
			bean.setSex(json.getInt("sex"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.UserSelfInfoBean JD-Core Version: 0.6.0
 */