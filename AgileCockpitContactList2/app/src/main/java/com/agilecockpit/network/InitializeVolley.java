package com.agilecockpit.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

/**
 * Created by chanchal Sharma.
 */
public class InitializeVolley {

    private static RequestQueue mRequestQueue;


    private InitializeVolley() {}

    /**
     *
     * initialize Volley
     */
    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context, new HurlStack(), false);
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            return mRequestQueue = Volley.newRequestQueue(context, new HurlStack(),false);
        }
    }
}
