package com.bakatumu.bakatumu.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bakatumu.bakatumu.model.User;

/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "androidhive_gcm";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ALAMAT = "user_alamat";
    private static final String KEY_USER_LAT = "user_lat";
    private static final String KEY_USER_LNG = "user_lng";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_APIKEY = "user_apiKey";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_ALAMAT, user.getAlamat());
        editor.putString(KEY_USER_LAT, user.getLat());
        editor.putString(KEY_USER_LNG, user.getLng());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_APIKEY, user.getApiKey());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getPhone());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, alamat, lat, lng, email, phone, apiKey, lastMessage, timestamp;
            int unreadCount;

            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            alamat = pref.getString(KEY_USER_ALAMAT, null);
            lat = pref.getString(KEY_USER_LAT, null);
            lng = pref.getString(KEY_USER_LNG, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            phone = pref.getString(KEY_USER_PHONE, null);
            apiKey = pref.getString(KEY_USER_APIKEY, null);
            lastMessage = null;
            timestamp = null;
            unreadCount = 0;

            User user = new User(id, name, alamat, lat, lng, email, phone, apiKey, lastMessage, timestamp, unreadCount);
            return user;
        }
        return null;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
