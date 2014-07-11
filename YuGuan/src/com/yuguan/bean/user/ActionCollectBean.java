package com.yuguan.bean.user;

import java.io.Serializable;

import org.json.JSONObject;
/**
 * 
 * @author charles.chen
 *
 */
public class ActionCollectBean implements Serializable {
	// /\*(\s+)\*/|/\*(\s+\d{1,}\s+)\*/
	private static final long serialVersionUID = 7394076082006494541L;
	private int id;
	private int unum;
	private String aname;
	private String mname;
	private String ftime;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUnum() {
		return this.unum;
	}

	public void setUnum(int unum) {
		this.unum = unum;
	}

	public String getAname() {
		return this.aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getMname() {
		return this.mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getFtime() {
		return this.ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	
	public static ActionCollectBean getBeanByJson(JSONObject json){
		try {
			ActionCollectBean bean = new ActionCollectBean();
			bean.setAname(json.getString("aname"));
			bean.setFtime(json.getString("ftime"));
			bean.setId(json.getInt("id"));
			bean.setMname(json.getString("mname"));
			bean.setUnum(json.getInt("unum"));
			return bean;
		} catch (Exception e) {
		}
		
		return null;
	}
	
}

