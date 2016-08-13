package com.mengshitech.colorrun.bean;

import java.io.Serializable;

/**
 * atenklsy
 */
public class HistoryEntity implements Serializable {
    private int Imageid;
    private String ActivityTitle;
    private String ActivityTime;
    private String ActivityLoc;
    private String imageposter;
    private int lerun_id;


    public int getImageid() {
        return Imageid;
    }

    public void setImageid(int imageid) {
        Imageid = imageid;
    }

    public String getActivityTitle() {
        return ActivityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        ActivityTitle = activityTitle;
    }

    public String getActivityTime() {
        return ActivityTime;
    }

    public void setActivityTime(String activityTime) {
        ActivityTime = activityTime;
    }

    public String getActivityLoc() {
        return ActivityLoc;
    }

    public void setActivityLoc(String activityLoc) {
        ActivityLoc = activityLoc;
    }

    public String getImageposter() {
        return imageposter;
    }

    public void setImageposter(String imageposter) {
        this.imageposter = imageposter;
    }

    public int getLerun_id() {
        return lerun_id;
    }

    public void setLerun_id(int lerun_id) {
        this.lerun_id = lerun_id;
    }
}
