package com.yuguan.bean;

import java.io.Serializable;

public class CountyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String flag;
	private String city;
	private int pid;
	
	
	public CountyBean() {
	}
	
	
	public CountyBean(int id, String flag, String city, int pid) {
		this.id = id;
		this.flag = flag;
		this.city = city;
		this.pid = pid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	

}
