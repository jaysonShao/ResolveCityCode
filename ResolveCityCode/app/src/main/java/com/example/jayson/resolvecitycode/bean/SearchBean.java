package com.example.jayson.resolvecitycode.bean;

import java.util.List;

/**
 * Created by jayson on 2018/3/8.
 */

public class SearchBean {

    private String type;//控制显示三个holder
    private String targer;//黑色省洲，市县，国家标签
    private List<SearchChildBean> childList;//省洲子listView
    private String cityName;

    private String oneSearchName;
    private String twoSearchName;
    private String threeSearchName;
    private String fourSearchName;


    private String oneSearchCode;
    private String twoSearchCode;
    private String threeSearchCode;
    private String fourSearchCode;


    public void setType(String type) {
        this.type = type;
    }

    public void setTarger(String targer) {
        this.targer = targer;
    }

    public void setChildList(List<SearchChildBean> childList) {
        this.childList = childList;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setOneSearchName(String oneSearchName) {
        this.oneSearchName = oneSearchName;
    }

    public void setTwoSearchName(String twoSearchName) {
        this.twoSearchName = twoSearchName;
    }

    public void setThreeSearchName(String threeSearchName) {
        this.threeSearchName = threeSearchName;
    }

    public void setFourSearchName(String fourSearchName) {
        this.fourSearchName = fourSearchName;
    }

    public void setOneSearchCode(String oneSearchCode) {
        this.oneSearchCode = oneSearchCode;
    }

    public void setTwoSearchCode(String twoSearchCode) {
        this.twoSearchCode = twoSearchCode;
    }

    public void setThreeSearchCode(String threeSearchCode) {
        this.threeSearchCode = threeSearchCode;
    }

    public void setFourSearchCode(String fourSearchCode) {
        this.fourSearchCode = fourSearchCode;
    }

    public String getType() {

        return type;
    }

    public String getTarger() {
        return targer;
    }

    public List<SearchChildBean> getChildList() {
        return childList;
    }

    public String getCityName() {
        return cityName;
    }

    public String getOneSearchName() {
        return oneSearchName;
    }

    public String getTwoSearchName() {
        return twoSearchName;
    }

    public String getThreeSearchName() {
        return threeSearchName;
    }

    public String getFourSearchName() {
        return fourSearchName;
    }

    public String getOneSearchCode() {
        return oneSearchCode;
    }

    public String getTwoSearchCode() {
        return twoSearchCode;
    }

    public String getThreeSearchCode() {
        return threeSearchCode;
    }

    public String getFourSearchCode() {
        return fourSearchCode;
    }
}
