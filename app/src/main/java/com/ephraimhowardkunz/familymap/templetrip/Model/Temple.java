package com.ephraimhowardkunz.familymap.templetrip.Model;

import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by apple on 4/19/16.
 */
public class Temple extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String webViewUrl;
    private String dedication; //Because it may read Construction, Renovation, etc.
    private String place;
    private String address;
    private String imageLink;
    private String telephone;
    private String firstLetter;
    private String localImagePath;
    private String dedicatoryPrayer;
    private boolean isFavorite;
    private boolean hasCafeteria;
    private boolean hasClothing;
    private boolean existsOnServer;
    private RealmList<RealmDate> closedDates;
    private RealmList<RealmDictionaryObject> endowmentSchedule;

    public Temple(){

    }

    public Temple(ParseObject o){
        this.setName(o.getString("name"));
        this.setWebViewUrl(o.getString("detailLink"));
        this.setImageLink(o.getString("photoLink"));
        this.setAddress(o.getString("address"));
        this.setTelephone(o.getString("telephone"));
        this.setPlace(o.getString("place"));
        this.setFirstLetter(o.getString("name").substring(0, 1));
        this.setDedication(o.getString("dedication"));
        this.setId(o.getObjectId());
        this.setDedicatoryPrayer(o.getString("prayer"));

       // this.localImagePath
        this.isFavorite = false;
        this.existsOnServer = true;

        try {
            //Don't need this information right now, and it doesn't exist on announced temples
//            this.hasCafeteria = !o.getJSONObject("servicesAvailable").getString("Cafeteria").contains("No");
//            this.hasClothing = !o.getJSONObject("servicesAvailable").getString("Clothing").contains("No");

            this.endowmentSchedule = new RealmList<>();
            JSONObject scheduleDict = o.getJSONObject("endowmentSchedule");
            Iterator<String> keys = scheduleDict.keys();
            while(keys.hasNext()){
                String key = keys.next();
                RealmDictionaryObject dictObj = new RealmDictionaryObject(key, scheduleDict.getString(key));
                endowmentSchedule.add(dictObj);
            }

            //Not using these right now, and Announced temples don't have it
//            closedDates = new RealmList<>();
//            JSONObject closedDateArrays = o.getJSONObject("closures");
//            JSONArray maintenance = closedDateArrays.getJSONArray("Maintenance Dates");
//            for(int i = 0; i < maintenance.length(); ++i){
//                closedDates.add(new RealmDate(maintenance.getString(i)));
//            }
//            JSONArray other = closedDateArrays.getJSONArray("Other Dates");
//            for(int i = 0; i < other.length(); ++i){
//                closedDates.add(new RealmDate(other.getString(i)));
//            }

        }
        catch (JSONException ex){
            assert false; // We must have bad json data on the server. Fix it.
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebViewUrl() {
        return webViewUrl;
    }

    public String getDedicatoryPrayer() { return dedicatoryPrayer;}

    public void setWebViewUrl(String webViewUrl) {
        this.webViewUrl = webViewUrl;
    }

    public String getDedication() {
        try{

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dedication);
            return DateFormat.getDateInstance().format(date);
        }
        catch (ParseException ex){
            //Must not be a good date string, so just show it as is
            return dedication;
        }
    }

    public void setDedication(String dedication) {
        this.dedication = dedication;
    }

    public String getPlace() {
        return place;
    }

    public void setDedicatoryPrayer(String dedicatoryPrayer){this.dedicatoryPrayer = dedicatoryPrayer;}

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isHasCafeteria() {
        return hasCafeteria;
    }

    public void setHasCafeteria(boolean hasCafeteria) {
        this.hasCafeteria = hasCafeteria;
    }

    public boolean isHasClothing() {
        return hasClothing;
    }

    public void setHasClothing(boolean hasClothing) {
        this.hasClothing = hasClothing;
    }

    public boolean isExistsOnServer() {
        return existsOnServer;
    }

    public void setExistsOnServer(boolean existsOnServer) {
        this.existsOnServer = existsOnServer;
    }

    public RealmList<RealmDate> getClosedDates() {
        return closedDates;
    }

    public void setClosedDates(RealmList<RealmDate> closedDates) {
        this.closedDates = closedDates;
    }

    public RealmList<RealmDictionaryObject> getEndowmentSchedule() {
        return endowmentSchedule;
    }

    public void setEndowmentSchedule(RealmList<RealmDictionaryObject> endowmentSchedule) {
        this.endowmentSchedule = endowmentSchedule;
    }
}
