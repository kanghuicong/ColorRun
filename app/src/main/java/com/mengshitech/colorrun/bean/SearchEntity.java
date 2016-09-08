package com.mengshitech.colorrun.bean;

import java.io.Serializable;

/**
 * Created by kk on 2016/9/7.
 */
public class SearchEntity implements Serializable {
    String user_id;
    String user_search;

    public SearchEntity(){
    }

    public SearchEntity(String user_id, String user_search) {
        this.user_id = user_id;
        this.user_search = user_search;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_search() {
        return user_search;
    }

    public void setUser_search(String user_search) {
        this.user_search = user_search;
    }
}
