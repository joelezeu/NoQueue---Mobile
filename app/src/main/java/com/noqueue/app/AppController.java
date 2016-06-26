package com.noqueue.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Joel on 5/26/2016.
 */
public class AppController extends Application {
    public static String TAG = AppController.class.getSimpleName();
    private RequestQueue requestQueue;
    private static AppController appController;

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
    }
    public static synchronized AppController getInstance(){
        return appController;
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(request);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (requestQueue!= null) {
            requestQueue.cancelAll(tag);
        }
    }
}
