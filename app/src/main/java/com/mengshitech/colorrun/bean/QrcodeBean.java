package com.mengshitech.colorrun.bean;

/**
 * @Project: LeRun
 * @Author: wschenyongyin
 * @Date: 2016年7月29日
 * @explain:二维码返回给客户端
 * @TestState:
 */

public class QrcodeBean {
    private String imagePath;
    private String personal_name;
    private int sign_state;
    private int charge_state;
    private String lerun_title;
    private int Payment;
    private String lerun_time;
    private int evaluate_state;
    private int lerun_id;
    private String user_telphone;
    private int check_state;

    public QrcodeBean() {
    }

    public int getCheck_state() {
        return check_state;
    }

    public void setCheck_state(int check_state) {
        this.check_state = check_state;
    }

    public String getUser_telphone() {
        return user_telphone;
    }

    public void setUser_telphone(String user_telphone) {
        this.user_telphone = user_telphone;
    }

    public int getLerun_id() {
        return lerun_id;
    }

    public void setLerun_id(int lerun_id) {
        this.lerun_id = lerun_id;
    }

    public int getEvaluate_state() {
        return evaluate_state;
    }

    public void setEvaluate_state(int evaluate_state) {
        this.evaluate_state = evaluate_state;
    }

    public String getLerun_time() {
        return lerun_time;
    }

    public void setLerun_time(String lerun_time) {
        this.lerun_time = lerun_time;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }

    public int getSign_state() {
        return sign_state;
    }

    public void setSign_state(int sign_state) {
        this.sign_state = sign_state;
    }

    public String getLerun_title() {
        return lerun_title;
    }

    public void setLerun_title(String lerun_title) {
        this.lerun_title = lerun_title;
    }

    public int getCharge_state() {
        return charge_state;
    }

    public void setCharge_state(int charge_state) {
        this.charge_state = charge_state;
    }

    public int getPayment() {
        return Payment;
    }

    public void setPayment(int payment) {
        Payment = payment;
    }


}
