package com.yuguan.bean;

import java.io.Serializable;

public class MallBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String address;
	private int comments;
	private int favorites;
	private String mdesc;
	private String phone;
	private String pic;
	private int score;
	private String title;

	public MallBean() {
	}

	public MallBean(int id, String address, int comments, int favorites,
			String mdesc, String phone, String pic, int score, String title) {
		super();
		this.id = id;
		this.address = address;
		this.comments = comments;
		this.favorites = favorites;
		this.mdesc = mdesc;
		this.phone = phone;
		this.pic = pic;
		this.score = score;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getFavorites() {
		return favorites;
	}

	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}

	public String getMdesc() {
		return mdesc;
	}

	public void setMdesc(String mdesc) {
		this.mdesc = mdesc;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
