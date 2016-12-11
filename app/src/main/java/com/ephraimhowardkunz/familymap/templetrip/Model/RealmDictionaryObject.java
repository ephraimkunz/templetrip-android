package com.ephraimhowardkunz.familymap.templetrip.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by apple on 4/19/16.
 */
public class RealmDictionaryObject extends RealmObject {
    @PrimaryKey
    private String key;

    private String value;

    public RealmDictionaryObject(){}

    public RealmDictionaryObject(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
