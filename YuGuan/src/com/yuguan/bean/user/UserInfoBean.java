package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;

import com.yuguan.util.LevelCalculator;

public class UserInfoBean implements Serializable {
	private static final long serialVersionUID = 4093730762129312060L;
	private Long uid;
	private String name;
	private String pic;
	private String addr;
	private int sex;
	private int rpu;
	private int skill;
	private int star;
	private String level;

	public Long getUid() {
		return this.uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

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

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getSex() {
		return this.sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getRpu() {
		return this.rpu;
	}

	public void setRpu(int rpu) {
		this.rpu = rpu;
		setStar(LevelCalculator.calRepatationStar(rpu));
	}

	public int getSkill() {
		return this.skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
		setLevel("LV" + LevelCalculator.calSkillLevel(skill));
	}

	public int getStar() {
		return this.star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public static UserInfoBean getBeanByJson(JSONObject json){
		try {
			UserInfoBean bean = new UserInfoBean();
			bean.setAddr(json.getString("addr"));
			bean.setLevel(json.getString("level"));
			bean.setName(json.getString("name"));
			bean.setPic(json.getString("pic"));
			bean.setRpu(json.getInt("rpu"));
			bean.setSex(json.getInt("sex"));
			bean.setSkill(json.getInt("skill"));
			bean.setStar(json.getInt("star"));
			bean.setUid(json.getLong("uid"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
}

/*
 * Location: C:\Users\charles.chen\Desktop\classes.zip Qualified Name:
 * com.yuqiu.bean.user.UserInfoBean JD-Core Version: 0.6.0
 */