package com.example.jayson.resolvecitycode.utils;


import com.example.jayson.resolvecitycode.bean.CityBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jayson on 2018/3/3.
 */

public class FirstClassifyList implements Serializable{
    private String firstCity;

    public String getFirstCity() {
        return firstCity;
    }

    public void setFirstCity(String firstCity) {
        this.firstCity = firstCity;
    }

    public List<CityBean> getSubclassifylist() {
        return subclassifylist;
    }

    public void setSubclassifylist(List<CityBean> subclassifylist) {
        this.subclassifylist = subclassifylist;
    }

    private List<CityBean> subclassifylist;
}
