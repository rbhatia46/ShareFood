package com.food.nofoodwaste.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by RamakrishnaAS on 7/23/2015.
 */
public class AppSharedPreferences {

    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;

    public AppSharedPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(MyConstants.PREF_NAME_GLOBAL, Activity.MODE_PRIVATE);

    }

    public void removePreferences(String key) {
        editPrefs();
        _prefsEditor.remove(key);
        commitPrefs();

    }

    public AppSharedPreferences(Context context, String prefname) {
        this._sharedPrefs = context.getSharedPreferences(prefname, Activity.MODE_PRIVATE);
    }

    public void editPrefs() {
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getStringPreferences(String key) {
        String mStringPrefs = _sharedPrefs.getString(key, "");
        return mStringPrefs;
    }

    public boolean getBooleanPreferences(String key) {
        boolean mBoleanPrefs = _sharedPrefs.getBoolean(key, false);
        return mBoleanPrefs;
    }

    public void saveStringPreferences(String key, String value) {
        Log.v("String App Preferences", "Pref Val: " + value);
        editPrefs();
        _prefsEditor.putString(key, value);
        commitPrefs();
    }

    public void saveBooleanPreferences(String key, Boolean value) {
        Log.v("Boolean App Preferences", "Pref Val: " + value);
        editPrefs();
        _prefsEditor.putBoolean(key, value);
        commitPrefs();
    }

    public void commitPrefs() {
        _prefsEditor.commit();
    }
}