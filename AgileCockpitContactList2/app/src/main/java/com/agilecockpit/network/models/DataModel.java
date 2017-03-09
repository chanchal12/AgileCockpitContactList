package com.agilecockpit.network.models;

import com.android.volley.VolleyError;

import java.io.Serializable;

public abstract class DataModel implements Serializable {

    public int httpStatusCode;
    public VolleyError volleyError;
    public String jsonString;

}
