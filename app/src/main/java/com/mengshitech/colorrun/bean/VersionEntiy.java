package com.mengshitech.colorrun.bean;
/**
 *@Project: LeRun
 *@Author: wschenyongyin
 *@Date: 2016年8月4日
 *@explain:版本更新表
 *@TestState:
 */

public class VersionEntiy {
	private int version_id;
	private String version_number;
	private String update_time;
	private String update_content;
	private String update_url;
	private String update_size;
	
	
	public VersionEntiy() {
		super();
		// TODO Auto-generated constructor stub
	}


	public VersionEntiy(int version_id, String version_number, String update_time,
						String update_content, String update_url) {
		super();
		this.version_id = version_id;
		this.version_number = version_number;
		this.update_time = update_time;
		this.update_content = update_content;
		this.update_url = update_url;
	}


	public int getVersion_id() {
		return version_id;
	}


	public void setVersion_id(int version_id) {
		this.version_id = version_id;
	}


	public String getVersion_number() {
		return version_number;
	}


	public void setVersion_number(String version_number) {
		this.version_number = version_number;
	}


	public String getUpdate_time() {
		return update_time;
	}


	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}


	public String getUpdate_content() {
		return update_content;
	}


	public void setUpdate_content(String update_content) {
		this.update_content = update_content;
	}


	public String getUpdate_url() {
		return update_url;
	}


	public void setUpdate_url(String update_url) {
		this.update_url = update_url;
	}

	public String getUpdate_size() {
		return update_size;
	}

	public void setUpdate_size(String update_size) {
		this.update_size = update_size;
	}

	@Override
	public String toString() {
		return "VersionEntiy{" +
				"version_id=" + version_id +
				", version_number='" + version_number + '\'' +
				", update_time='" + update_time + '\'' +
				", update_content='" + update_content + '\'' +
				", update_url='" + update_url + '\'' +
				", update_size='" + update_size + '\'' +
				'}';
	}
}
