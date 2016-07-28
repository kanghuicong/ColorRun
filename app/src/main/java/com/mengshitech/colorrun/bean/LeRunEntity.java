package com.mengshitech.colorrun.bean;

public class LeRunEntity {
	private int lerun_id;
	private String lerun_title;
	private String lerun_content;
	private String lerun_poster;
	private String lerun_time;
	private String lerun_map;
	private String lerun_routine;
	private String lerun_host;
	private int charge_id;
	private String lerun_process;
	private String lerun_ruler;
	private int lerun_state;
	private String lerun_type;
	private String lerun_dimage;
	private String lerun_address;
	private String lerun_city;
	private String lerun_sponsor;
	private int lerun_maxuser;
	private int freecharge_number;
	private String lerun_video;
	private String lerun_begintime;
	private String lerun_endtime;
	private int charge_mode;
	private int insurance_id;
	//活动收费表
	private int charge_free;
	private int charge_common;
	private int charge_vip;
	private String free_equipment;
	private String common_equipment;
	private String vip_eqeuipment;
	//保险配置
	private String insurance_name;
	private String pay_money;
	private String insurance_money;
	private String insurancecompany_name;
	
	//剩余
	private int lerun_surplus;

	
	public LeRunEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	

	public LeRunEntity(int lerun_id, String lerun_title, String lerun_poster,
			String lerun_time, String lerun_address,int lerun_state) {
		super();
		this.lerun_id = lerun_id;
		this.lerun_title = lerun_title;
		this.lerun_poster = lerun_poster;
		this.lerun_time = lerun_time;
		this.lerun_address = lerun_address;
		this.lerun_state=lerun_state;
	}


	public LeRunEntity(int lerun_id, String lerun_title, String lerun_content,
			String lerun_poster, String lerun_time, String lerun_map,
			String lerun_routine, String lerun_host, int charge_id,
			String lerun_process, String lerun_ruler, int lerun_state,
			String lerun_type, String lerun_dimage, String lerun_address,
			String lerun_city, String lerun_sponsor, int lerun_maxuser,
			int freecharge_number, String lerun_video, String lerun_begintime,
			String lerun_endtime, int charge_mode) {
		super();
		this.lerun_id = lerun_id;
		this.lerun_title = lerun_title;
		this.lerun_content = lerun_content;
		this.lerun_poster = lerun_poster;
		this.lerun_time = lerun_time;
		this.lerun_map = lerun_map;
		this.lerun_routine = lerun_routine;
		this.lerun_host = lerun_host;
		this.charge_id = charge_id;
		this.lerun_process = lerun_process;
		this.lerun_ruler = lerun_ruler;
		this.lerun_state = lerun_state;
		this.lerun_type = lerun_type;
		this.lerun_dimage = lerun_dimage;
		this.lerun_address = lerun_address;
		this.lerun_city = lerun_city;
		this.lerun_sponsor = lerun_sponsor;
		this.lerun_maxuser = lerun_maxuser;
		this.freecharge_number = freecharge_number;
		this.lerun_video = lerun_video;
		this.lerun_begintime = lerun_begintime;
		this.lerun_endtime = lerun_endtime;
		this.charge_mode = charge_mode;
	}


	public int getLerun_surplus() {
		return lerun_surplus;
	}





	public void setLerun_surplus(int lerun_surplus) {
		this.lerun_surplus = lerun_surplus;
	}





	public int getInsurance_id() {
		return insurance_id;
	}


	public void setInsurance_id(int insurance_id) {
		this.insurance_id = insurance_id;
	}


	public int getLerun_id() {
		return lerun_id;
	}


	public void setLerun_id(int lerun_id) {
		this.lerun_id = lerun_id;
	}


	public String getLerun_title() {
		return lerun_title;
	}


	public void setLerun_title(String lerun_title) {
		this.lerun_title = lerun_title;
	}


	public String getLerun_content() {
		return lerun_content;
	}


	public void setLerun_content(String lerun_content) {
		this.lerun_content = lerun_content;
	}


	public String getLerun_poster() {
		return lerun_poster;
	}


	public void setLerun_poster(String lerun_poster) {
		this.lerun_poster = lerun_poster;
	}


	public String getLerun_time() {
		return lerun_time;
	}


	public void setLerun_time(String lerun_time) {
		this.lerun_time = lerun_time;
	}


	public String getLerun_map() {
		return lerun_map;
	}


	public void setLerun_map(String lerun_map) {
		this.lerun_map = lerun_map;
	}


	public String getLerun_routine() {
		return lerun_routine;
	}


	public void setLerun_routine(String lerun_routine) {
		this.lerun_routine = lerun_routine;
	}


	public String getLerun_host() {
		return lerun_host;
	}


	public void setLerun_host(String lerun_host) {
		this.lerun_host = lerun_host;
	}


	public int getCharge_id() {
		return charge_id;
	}


	public void setCharge_id(int charge_id) {
		this.charge_id = charge_id;
	}


	public String getLerun_process() {
		return lerun_process;
	}


	public void setLerun_process(String lerun_process) {
		this.lerun_process = lerun_process;
	}


	public String getLerun_ruler() {
		return lerun_ruler;
	}


	public void setLerun_ruler(String lerun_ruler) {
		this.lerun_ruler = lerun_ruler;
	}


	public int getLerun_state() {
		return lerun_state;
	}


	public void setLerun_state(int lerun_state) {
		this.lerun_state = lerun_state;
	}


	public String getLerun_type() {
		return lerun_type;
	}


	public void setLerun_type(String lerun_type) {
		this.lerun_type = lerun_type;
	}


	public String getLerun_dimage() {
		return lerun_dimage;
	}


	public void setLerun_dimage(String lerun_dimage) {
		this.lerun_dimage = lerun_dimage;
	}


	public String getLerun_address() {
		return lerun_address;
	}


	public void setLerun_address(String lerun_address) {
		this.lerun_address = lerun_address;
	}


	public String getLerun_city() {
		return lerun_city;
	}


	public void setLerun_city(String lerun_city) {
		this.lerun_city = lerun_city;
	}


	public String getLerun_sponsor() {
		return lerun_sponsor;
	}


	public void setLerun_sponsor(String lerun_sponsor) {
		this.lerun_sponsor = lerun_sponsor;
	}


	public int getLerun_maxuser() {
		return lerun_maxuser;
	}


	public void setLerun_maxuser(int lerun_maxuser) {
		this.lerun_maxuser = lerun_maxuser;
	}


	public int getFreecharge_number() {
		return freecharge_number;
	}


	public void setFreecharge_number(int freecharge_number) {
		this.freecharge_number = freecharge_number;
	}


	public String getLerun_video() {
		return lerun_video;
	}


	public void setLerun_video(String lerun_video) {
		this.lerun_video = lerun_video;
	}


	public String getLerun_begintime() {
		return lerun_begintime;
	}


	public void setLerun_begintime(String lerun_begintime) {
		this.lerun_begintime = lerun_begintime;
	}


	public String getLerun_endtime() {
		return lerun_endtime;
	}


	public void setLerun_endtime(String lerun_endtime) {
		this.lerun_endtime = lerun_endtime;
	}


	public int getCharge_mode() {
		return charge_mode;
	}


	public void setCharge_mode(int charge_mode) {
		this.charge_mode = charge_mode;
	}


	public int getCharge_free() {
		return charge_free;
	}


	public void setCharge_free(int charge_free) {
		this.charge_free = charge_free;
	}


	public int getCharge_common() {
		return charge_common;
	}


	public void setCharge_common(int charge_common) {
		this.charge_common = charge_common;
	}


	public int getCharge_vip() {
		return charge_vip;
	}


	public void setCharge_vip(int charge_vip) {
		this.charge_vip = charge_vip;
	}


	public String getFree_equipment() {
		return free_equipment;
	}


	public void setFree_equipment(String free_equipment) {
		this.free_equipment = free_equipment;
	}


	public String getCommon_equipment() {
		return common_equipment;
	}


	public void setCommon_equipment(String common_equipment) {
		this.common_equipment = common_equipment;
	}


	public String getVip_eqeuipment() {
		return vip_eqeuipment;
	}


	public void setVip_eqeuipment(String vip_eqeuipment) {
		this.vip_eqeuipment = vip_eqeuipment;
	}


	public String getInsurance_name() {
		return insurance_name;
	}


	public void setInsurance_name(String insurance_name) {
		this.insurance_name = insurance_name;
	}


	public String getPay_money() {
		return pay_money;
	}


	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}


	public String getInsurance_money() {
		return insurance_money;
	}


	public void setInsurance_money(String insurance_money) {
		this.insurance_money = insurance_money;
	}


	public String getInsurancecompany_name() {
		return insurancecompany_name;
	}


	public void setInsurancecompany_name(String insurancecompany_name) {
		this.insurancecompany_name = insurancecompany_name;
	}
	
	
	

	
}
