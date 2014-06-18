package com.yuguan.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class CommentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String comment;
	private int id;
	private String postTime;
	private String uName;
	private String uPic;
	private int uid;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuPic() {
		return uPic;
	}

	public void setuPic(String uPic) {
		this.uPic = uPic;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public static CommentBean getCommentBean(JSONObject json) throws Exception {
		CommentBean bean = new CommentBean();
		bean.setComment(json.getString("comment"));
		bean.setId(json.getInt("id"));
		bean.setPostTime(json.getString("postTime"));
		bean.setUid(json.getInt("uid"));
		bean.setuName(json.getString("uName"));
		bean.setuPic(json.getString("uPic"));
		return bean;
	}

}
