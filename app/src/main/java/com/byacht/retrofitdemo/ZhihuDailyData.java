package com.byacht.retrofitdemo;

import com.byacht.retrofitdemo.entity.ZhihuDaily;

import java.util.ArrayList;

/**
 * Created by dn on 2017/4/8.
 */

public class ZhihuDailyData {
    private String date;
    private ArrayList<ZhihuDaily> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ZhihuDaily> getStories() {
        return stories;
    }

    public void setStories(ArrayList<ZhihuDaily> stories) {
        this.stories = stories;
    }

}
