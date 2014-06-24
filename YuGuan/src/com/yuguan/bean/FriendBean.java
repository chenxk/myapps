package com.yuguan.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class FriendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String addr;
	private String level;
	private String name;
	private String pic;
	private int rpu;
	private int sex;
	private int skill;
	private int star;
	private int uid;

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

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

	public int getRpu() {
		return rpu;
	}

	public void setRpu(int rpu) {
		this.rpu = rpu;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public static FriendBean getBeanFromJson(JSONObject json){
		FriendBean bean = new FriendBean();
		try {
			bean.setAddr(json.getString("addr"));
			bean.setLevel(json.getString("level"));
			bean.setName(json.getString("name"));
			bean.setPic(json.getString("pic"));
			bean.setRpu(json.getInt("rpu"));
			bean.setSex(json.getInt("sex"));
			bean.setSkill(json.getInt("skill"));
			bean.setStar(json.getInt("star"));
			bean.setUid(json.getInt("uid"));
			return bean;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

}
