package com.noqueue.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Joel on 5/26/2016.
 */
public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    SharedPreferences sharedPreferences;
    Context _context;
    SharedPreferences.Editor editor;

    public static final String SHARED_PREF_NAME = "NoQueue";
    public static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context){
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, 0);
        editor = sharedPreferences.edit();
    }
    public void setLogin(boolean login){
        editor.putBoolean(KEY_IS_LOGGEDIN, login);
        editor.commit();
        Log.d(TAG, "User loggin session modoified");
    }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
