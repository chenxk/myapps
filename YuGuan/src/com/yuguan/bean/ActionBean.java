package com.yuguan.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class ActionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bTime;
	private int id;
	private String mall;
	private String pic;
	private String score;
	private String title;

	public ActionBean() {
	}

	public ActionBean(String bTime, int id, String mall, String pic,
			String score, String title) {
		this.bTime = bTime;
		this.id = id;
		this.mall = mall;
		this.pic = pic;
		this.score = score;
		this.title = title;
	}

	public String getbTime() {
		return bTime;
	}

	public void setbTime(String bTime) {
		this.bTime = bTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMall() {
		return mall;
	}

	public void setMall(String mall) {
		this.mall = mall;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static ActionBean getActionBean(JSONObject json) {

		try {
			ActionBean action = new ActionBean();
			action.setbTime(json.getString("bTime"));
			action.setId(json.getInt("id"));
			action.setMall(json.getString("mall"));
			action.setPic(json.getString("pic"));
			action.setScore(json.getString("score"));
			action.setTitle(json.getString("title"));

			return action;
		} catch (Exception e) {
		}

		return null;
	}

}
