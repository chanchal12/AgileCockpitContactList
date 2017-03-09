package com.agilecockpit.network;

import android.content.Context;

import com.agilecockpit.network.models.ContactListDto;
import com.agilecockpit.utilities.Constants;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestCreator {


    private static class CreatorHolder {
        private static RequestCreator INSTANCE = new RequestCreator();
    }

    public static RequestCreator getInstance() {
        return CreatorHolder.INSTANCE;
    }
    /**
     * Request to contact list using token
     *
     * @param context
     * @return
     */
    public InterceptorRequest getContactList(Context context) {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.PROTOCOL_CONTENT_TYPE_APPLICATION_URL_ENCODED);
        String url = Constants.SERVER_URL ;
        String stringParams = Constants.TOKEN + "=" + Constants.TOKEN_VALUE;
        return new InterceptorRequest(context, Request.Method.POST, url, stringParams, params,
                headers, new ContactListDto());
    }




}
