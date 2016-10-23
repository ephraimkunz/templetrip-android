package com.ephraimhowardkunz.familymap.templetrip.Model;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.ephraimhowardkunz.familymap.templetrip.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by apple on 4/19/16.
 */
public class DataManager {
    private final static String TAG = "DataManager";
    private static final String DEFAULT_DIRECTORY = "imageDirectory";
    private static boolean realmInitialized = false;
    private static boolean parseInitialized = false;

    public interface ImportFinishedCallback{
        void onImportFinished();
    }

    public static void importFromParseIntoRealm(final Context context, final ImportFinishedCallback callback){
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
                callback.onImportFinished();
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
        RealmResults<Temple> allTemples = Realm.getDefaultInstance().where(Temple.class).findAllSorted("name");
        return convertRealmResultsToArrayList(allTemples);
    }

    public static List<Temple> getTemplesByFilterText(String filterText){
        RealmResults<Temple> filtered = Realm.getDefaultInstance()
                .where(Temple.class)
                .contains("name", filterText, Case.INSENSITIVE)
                .findAllSorted("name");
        return convertRealmResultsToArrayList(filtered);
    }

    public static List<Temple> convertRealmResultsToArrayList(RealmResults<Temple> realmResults){
        List<Temple> results = new ArrayList<>();
        for(int i = 0; i < realmResults.size(); ++i){
            results.add(realmResults.get(i));
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

    public static void getTempleImage(Context context, ImageView imageView, Temple temple){
        if(temple.getLocalImagePath() == null){
            //Fetch from web and cache
            new DownloadImageTask(context, imageView, temple.getId()).execute(temple.getImageLink());
        }
        else{
            //Fetch from local cache
            Bitmap bitmap = loadImageFromStorage(temple.getLocalImagePath());
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void saveToInternalStorage(Context context, Bitmap bitmapImage, String templeId){
        ContextWrapper cw = new ContextWrapper(context);
        Temple temple = Realm.getDefaultInstance().where(Temple.class).equalTo("id", templeId).findFirst();

        // path to /data/data/yourapp/app_data/DEFAULT_DIRECTORY
        File directory = cw.getDir(DEFAULT_DIRECTORY, Context.MODE_PRIVATE);
        String imageName = temple.getName().replace(' ', '_') + ".png";
        File mypath = new File(directory,imageName);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Realm.getDefaultInstance().beginTransaction();
        temple.setLocalImagePath(mypath.toString());
        Realm.getDefaultInstance().commitTransaction();
    }

    private static Bitmap loadImageFromStorage(String path)
    {
        try {
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
