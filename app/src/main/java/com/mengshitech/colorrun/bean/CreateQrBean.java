package com.mengshitech.colorrun.bean;

public class CreateQrBean {
	int lerun_id;
	String user_id;
	String user_telphone;

	public String getUser_telphone() {
		return user_telphone;
	}

	public void setUser_telphone(String user_telphone) {
		this.user_telphone = user_telphone;
	}

	public CreateQrBean() {
	}

	public int getLerun_id() {
		return lerun_id;
	}

	public void setLerun_id(int lerun_id) {
		this.lerun_id = lerun_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
