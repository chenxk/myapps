package com.yuguan.bean;

import org.json.JSONObject;

public class AccountInfo {

	private String message;
	private String userName;
	private String password;
	private String email;
	private int id;
	private String name;
	private String md5pwd;
	private int status;
	private int vip;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getMd5pwd() {
		return md5pwd;
	}
	public void setMd5pwd(String md5pwd) {
		this.md5pwd = md5pwd;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	
	public static AccountInfo getAccountInfo(JSONObject json){
		try {
			AccountInfo account = new AccountInfo();
			JSONObject user = json.getJSONObject("user");
			account.setEmail(user.getString("email"));
			account.setId(user.getInt("id"));
			account.setName(user.getString("name"));
			account.setMd5pwd(user.getString("password"));
			account.setStatus(user.getInt("status"));
			account.setVip(user.getInt("vip"));
			account.setMessage(json.getString("message"));
			account.setUserName(json.getString("username"));
			account.setPassword(json.getString("password"));
			return account;
		} catch (Exception e) {
		}
		
		return null;
	}
	
}
