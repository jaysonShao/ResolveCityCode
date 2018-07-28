package com.example.jayson.resolvecitycode.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by jayson on 2018/5/14.
 */

public class AddressChineseBean extends RealmObject implements Serializable {
    private String chinese;

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getChinese() {

        return chinese;
    }
}
