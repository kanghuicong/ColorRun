package com.mengshitech.colorrun.bean;

/**
 * @Project: LeRun
 * @Author: wschenyongyin
 * @Date: 2016年8月4日
 * @explain:版本更新表
 * @TestState:
 */

public class VersionEntiy {
    private int version_id;
    private int versionCode;
    private String versionName;
    private String update_time;
    private String update_content;
    private String update_url;
    private String update_size;


    public VersionEntiy() {
        super();
        // TODO Auto-generated constructor stub
    }

    public VersionEntiy(int version_id, int versionCode, String versionName, String update_time, String update_content, String update_url, String update_size) {
        this.version_id = version_id;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.update_time = update_time;
        this.update_content = update_content;
        this.update_url = update_url;
        this.update_size = update_size;
    }

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
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
}
