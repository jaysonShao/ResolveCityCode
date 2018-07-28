package com.example.jayson.resolvecitycode.bean;

import io.realm.RealmObject;

/**
 * Created by jayson on 2018/5/21.
 */

public class AddressInternationBean  extends RealmObject {

    private String interNation;

    public void setInterNation(String interNation) {
        this.interNation = interNation;
    }

    public String getInterNation() {

        return interNation;
    }
}
