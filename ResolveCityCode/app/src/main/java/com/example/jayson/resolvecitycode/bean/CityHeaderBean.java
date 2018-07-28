package com.example.jayson.resolvecitycode.bean;




import com.example.jayson.resolvecitycode.indexbar.BaseIndexPinyinBean;

import java.util.List;


public class CityHeaderBean extends BaseIndexPinyinBean {
    private List<FourCityBean> cityList;
    //悬停ItemDecoration显示的Tag
    private String suspensionTag;

    public CityHeaderBean() {
    }

    public CityHeaderBean(List<FourCityBean> cityList, String suspensionTag, String indexBarTag) {
        this.cityList = cityList;
        this.suspensionTag = suspensionTag;
        this.setBaseIndexTag(indexBarTag);
    }

    public List<FourCityBean> getCityList() {
        return cityList;
    }

    public CityHeaderBean setCityList(List<FourCityBean> cityList) {
        this.cityList = cityList;
        return this;
    }

    public CityHeaderBean setSuspensionTag(String suspensionTag) {
        this.suspensionTag = suspensionTag;
        return this;
    }

    @Override
    public String getTarget() {
        return null;
    }

    @Override
    public boolean isNeedToPinyin() {
        return false;
    }

    @Override
    public String getSuspensionTag() {
        return suspensionTag;
    }


}
