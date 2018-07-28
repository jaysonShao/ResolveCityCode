package com.example.jayson.resolvecitycode.bean;



import com.example.jayson.resolvecitycode.indexbar.BaseIndexPinyinBean;

import java.io.Serializable;


public class ZBeanCity extends BaseIndexPinyinBean implements Serializable{


    /**
     * id : 1
     * code : 10000
     * parent : 0
     * name : 中国
     * type : 0
     * latitude :
     * longitude :
     * deltime : 0
     */

    private String id;
    private String code;
    private String parent;
    private String name;
    private String type;
    private String pinyin;
    private String name_en;
    private int havs = 0;

    public void setHavs(int havs) {
        this.havs = havs;
    }

    public int getHavs() {

        return havs;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_en() {

        return name_en;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    //    public String getPinyin() {
//        return CharacterParser.getInstance().getSelling(name);
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getTarget() {
        return name;
    }
}
