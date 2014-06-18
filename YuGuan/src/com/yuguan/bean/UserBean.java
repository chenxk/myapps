package com.yuguan.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private int sex;
	private String address;
	private String pic;
	private int sort;
	private int uid;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public UserBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static UserBean getBeanFromJson(JSONObject json) {
		try {
			UserBean action = new UserBean();
			try {
				action.setId(json.getInt("id"));
			} catch (Exception e) {
				action.setId(0);
			}
			
			try {
				action.setUid(json.getInt("uid"));
			} catch (Exception e) {
				action.setUid(0);
			}
			try {
				action.setAddress(json.getString("address"));
			} catch (Exception e) {
				action.setAddress("");
			}
			try {
				action.setPic(json.getString("pic"));
			} catch (Exception e) {
				action.setPic("default.jpg");
			}
			try {
				action.setSex(json.getInt("sex"));
			} catch (Exception e) {
				action.setSex(0);
			}
			
			try {
				action.setSort(json.getInt("sort"));
			} catch (Exception e) {
				action.setSort(0);
			}
			action.setName(json.getString("name"));
			return action;
		} catch (Exception e) {
		}
		return null;
	}

}
