package com.ephraimhowardkunz.familymap.templetrip.Model;

import android.content.Context;
import android.util.Log;

import com.ephraimhowardkunz.familymap.templetrip.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by apple on 4/19/16.
 */
public class DataManager {
    private final static String TAG = "DataManager";
    private static boolean realmInitialized = false;
    private static boolean parseInitialized = false;

    public static void importFromParseIntoRealm(final Context context){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Temple");
        query.setLimit(500);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    for (ParseObject o: objects) {
                        saveToRealm(context, o);
                    }
                }
                else{
                    Log.e(TAG, "Failed to fetch temples from Parse");
                }
            }
        });
    }

    public static void saveToRealm(Context context, ParseObject o){
        Temple temple = new Temple(o);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(temple);
        realm.commitTransaction();
    }

    public static List<Temple> getAllTemplesFromRealm(Context context){
        RealmResults<Temple> allTemples = Realm.getDefaultInstance().allObjects(Temple.class);
        allTemples.sort("name", Sort.ASCENDING);

        List<Temple> results = new ArrayList<>();
        for(int i = 0; i < allTemples.size(); ++i){
            results.add(allTemples.get(i));
        }
        return results;
    }

    public static Temple getTempleById(Context context, String id){
        return Realm.getDefaultInstance().where(Temple.class).equalTo("id", id).findFirst();
    }

    public static void setUpParseAndRealm(Context context){
        if(!realmInitialized) {
            RealmConfiguration config = new RealmConfiguration.Builder(context)
                    .deleteRealmIfMigrationNeeded() //TODO: Change this in production
                    .build();
            Realm.setDefaultConfiguration(config);
        }
        realmInitialized = true;

        if(!parseInitialized) {
            String appId = context.getResources().getString(R.string.parseApplicationId);
            String serverUrl = context.getResources().getString(R.string.parseServerUrl);

            Parse.initialize(new Parse.Configuration.Builder(context)
                    .applicationId(appId)
                    .server(serverUrl)
                    .build());
        }
        parseInitialized = true;
    }
}
