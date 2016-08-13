package com.mengshitech.colorrun.bean;
/**
 *@Project: LeRun
 *@Author: wschenyongyin
 *@Date: 2016年8月10日
 *@explain:
 *@TestState:
 */

public class LeRunEvaluateEntity {
	int evaluate_id;
	int lerun_id;
	String user_id;
	int evaluate_star;
	String evaluate_content;
	String evaluate_time;
	
	
	//用户的头像 昵称
	private String user_name;
	private String user_header;
	
	
	
	
	public LeRunEvaluateEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


	public int getEvaluate_id() {
		return evaluate_id;
	}
	public void setEvaluate_id(int evaluate_id) {
		this.evaluate_id = evaluate_id;
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
	public int getEvaluate_star() {
		return evaluate_star;
	}
	public void setEvaluate_star(int evaluate_star) {
		this.evaluate_star = evaluate_star;
	}
	public String getEvaluate_content() {
		return evaluate_content;
	}
	public void setEvaluate_content(String evaluate_content) {
		this.evaluate_content = evaluate_content;
	}
	public String getEvaluate_time() {
		return evaluate_time;
	}
	public void setEvaluate_time(String evaluate_time) {
		this.evaluate_time = evaluate_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_header() {
		return user_header;
	}
	public void setUser_header(String user_header) {
		this.user_header = user_header;
	}
	
	

}
