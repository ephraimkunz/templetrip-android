package com.ephraimhowardkunz.familymap.templetrip.Model;

import android.content.Context;
import android.util.Log;

import com.ephraimhowardkunz.familymap.templetrip.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by apple on 4/19/16.
 */
public class ParseImporter {
    private final static String TAG = "ParseImporter";

    public static void importFromParseIntoRealm(final Context context){
        String appId = context.getResources().getString(R.string.parseApplicationId);
        String serverUrl = context.getResources().getString(R.string.parseServerUrl);

        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(appId)
                .server(serverUrl)
                .build());

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

        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
        Realm realm = Realm.getInstance(config);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(temple);
        realm.commitTransaction();
    }
}
