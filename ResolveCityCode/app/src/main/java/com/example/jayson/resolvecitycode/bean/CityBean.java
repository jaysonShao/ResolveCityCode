package com.example.jayson.resolvecitycode.bean;




import com.example.jayson.resolvecitycode.indexbar.BaseIndexPinyinBean;

import java.io.Serializable;

public class CityBean extends BaseIndexPinyinBean implements Serializable{
    private String city;//城市名字
    private String one;
    private String two;
    private String three;
    private String fout;

    private String codeOne;
    private String codeTwo;
    private String codeThree;
    private String codeFour;

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public void setCodeOne(String codeOne) {
        this.codeOne = codeOne;
    }

    public void setCodeTwo(String codeTwo) {
        this.codeTwo = codeTwo;
    }

    public void setCodeThree(String codeThree) {
        this.codeThree = codeThree;
    }

    public void setCodeFour(String codeFour) {
        this.codeFour = codeFour;
    }

    public String getCodeOne() {
        return codeOne;
    }

    public String getCodeTwo() {
        return codeTwo;
    }

    public String getCodeThree() {
        return codeThree;
    }

    public String getCodeFour() {
        return codeFour;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public void setFout(String fout) {
        this.fout = fout;
    }

    public String getThree() {
        return three;
    }

    public String getFout() {
        return fout;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "city='" + city + '\'' +
                '}';
    }

    public CityBean() {
    }

    public CityBean(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public CityBean setCity(String city) {
        this.city = city;
        return this;
    }

    @Override
    public String getTarget() {
        return city;
    }
}
