package com.example.harvey.simpletodo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by harvey on 6/14/16.
 */
public class ItemData extends RealmObject{
    @PrimaryKey
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
