package com.example.jayson.resolvecitycode.bean;


import com.example.jayson.resolvecitycode.indexbar.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * Created by jayson on 2018/3/7.
 */

public class FourCityBean extends BaseIndexPinyinBean implements Serializable {
    private String one = "0";
    private String two = "0";
    private String three = "0";
    private String four = "0";

    private String oneCode = "0";
    private String twoCode = "0";
    private String threeCode = "0";
    private String fourCode = "0";

    private String city = "0";

    private String parent = "0";


    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {

        return parent;
    }

    public void setOneCode(String oneCode) {
        this.oneCode = oneCode;
    }

    public void setTwoCode(String twoCode) {
        this.twoCode = twoCode;
    }

    public void setThreeCode(String threeCode) {
        this.threeCode = threeCode;
    }

    public void setFourCode(String fourCode) {
        this.fourCode = fourCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOneCode() {

        return oneCode;
    }

    public String getTwoCode() {
        return twoCode;
    }

    public String getThreeCode() {
        return threeCode;
    }

    public String getFourCode() {
        return fourCode;
    }

    public String getCity() {
        return city;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getOne() {
        return one;
    }

    public String getTwo() {
        return two;
    }

    public String getThree() {
        return three;
    }

    public String getFour() {
        return four;
    }

    @Override
    public String getTarget() {
        return city;
    }
}
