package com.example.jayson.resolvecitycode.bean;

import java.io.Serializable;

import io.realm.RealmObject;



public class JsonStringBean extends RealmObject implements Serializable {

    private String chinaName;
    private String internationalName;
    private String oneType;
    private String twoType;
    private String threeType;
    private String fourType;

    public void setFourType(String fourType) {
        this.fourType = fourType;
    }

    public String getFourType() {

        return fourType;
    }

    public void setOneType(String oneType) {
        this.oneType = oneType;
    }

    public void setTwoType(String twoType) {
        this.twoType = twoType;
    }

    public void setThreeType(String threeType) {
        this.threeType = threeType;
    }

    public String getOneType() {

        return oneType;
    }

    public String getTwoType() {
        return twoType;
    }

    public String getThreeType() {
        return threeType;
    }

    public void setChinaName(String chinaName) {
        this.chinaName = chinaName;
    }

    public void setInternationalName(String internationalName) {
        this.internationalName = internationalName;
    }

    public String getChinaName() {

        return chinaName;
    }

    public String getInternationalName() {
        return internationalName;
    }
}
