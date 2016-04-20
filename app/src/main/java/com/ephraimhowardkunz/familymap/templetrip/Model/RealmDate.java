package com.ephraimhowardkunz.familymap.templetrip.Model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by apple on 4/19/16.
 */
public class RealmDate extends RealmObject {
    @Ignore
    private String TAG = "RealmDate";

    private Date date;

    public RealmDate(){
        date = new Date();
    }

    public RealmDate(String dateString) {

        try {
            //2000-10-01
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(dateString);
        }
        catch(ParseException pe) {
            Log.w(TAG, String.format("Error parsing date string {0} into date", dateString));
            //throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString(){
        if(date != null)
            return DateFormat.getDateInstance().format(date);
        return "date is null";
    }
}
