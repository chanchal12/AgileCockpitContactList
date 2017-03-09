package com.agilecockpit.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.agilecockpit.network.models.DataModel;
import com.agilecockpit.utilities.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class InterceptorRequest extends Request<DataModel> {
    private static final String TAG = InterceptorRequest.class
            .getSimpleName();


    private byte[] mImageBody;


    /**
     * Maximum retry for a request. By default it will be 1.
     */
    private int mMaxRetryCount = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

    private Map<String, String> mRequestParams;
    private Map<String, String> mRequestHeaders;
    private String mRequestBody;
    private String mRequestTime, mResponseTime;
    private Gson mGson;
    private DataModel mResponseModel;
    private EventBus mEventBus = EventBus.getDefault();
    private Context mContext;

    private static Response.ErrorListener mErrorListener;

    {
        mErrorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(mContext,"Time out",Toast.LENGTH_SHORT).show();
                }
                Log.i("Error", error.toString());
                mResponseModel.volleyError = error;
                mEventBus.postSticky(mResponseModel);
            }
        };
    }

    public InterceptorRequest(Context context, int method, String url, String body,
                              Map<String, String> params, Map<String, String> headers,
                              DataModel responseModel) {
        super(method, url, mErrorListener);
        mContext = context;
        mRequestBody = body;
        mRequestHeaders = headers;
        mRequestParams = params;
        mResponseModel = responseModel;
        mGson = new Gson();
        logRequest(method, url);
    }

    @Override
    protected void deliverResponse(DataModel response) {
        if (mEventBus != null)
            mEventBus.postSticky(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);

        mResponseModel.volleyError = error;
        if (mEventBus != null)
            mEventBus.postSticky(mResponseModel);
    }

    @Override
    protected Response<DataModel> parseNetworkResponse(NetworkResponse response) {
        String jsonString = new String(response.data);
        Log.i("Response", jsonString );
        Log.i("Response status code", response.statusCode +"" );
        // for logs to check the time interval between request and response time
        if (TextUtils.isEmpty(jsonString)) {
            jsonString = new JsonObject().toString();
        }
        DataModel dataModel = mGson.fromJson(jsonString, mResponseModel.getClass());
        dataModel.httpStatusCode = response.statusCode;
        dataModel.jsonString = jsonString;
        return Response.success(dataModel, getCacheEntry());
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }

        return volleyError;
    }
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mImageBody != null) {
            return mImageBody;
        }
        return mRequestBody != null ? mRequestBody.getBytes() : super.getBody();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mRequestParams != null ? mRequestParams : super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mRequestHeaders != null ? mRequestHeaders : super.getHeaders();
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        retryPolicy = new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT_MS,
                mMaxRetryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        return super.setRetryPolicy(retryPolicy);
    }
    /**
     * Log for request and response
     * @param method
     * @param url
     */

    private void logRequest(int method, String url) {
        switch (method) {

            case Method.GET:
                Log.d(TAG, "Method : GET  ");

                break;
            case Method.POST:
                Log.d(TAG, "Method : POST ");
                break;
            default:
                break;
        }
        Log.d(TAG, "Request Url : " + url);
        Log.d(TAG, "Request Headers : ");
        logHeaders(mRequestHeaders);
        Log.d(TAG, "Request Params : ");
        logParams(mRequestParams);
        logRequestBodyContent();
        mRequestTime = currentDateTime();

    }

    private void logRequestBodyContent() {
        Log.d(TAG, "Body Content : " + mRequestBody);
    }

    private void logHeaders(Map<String, String> headers) {
        Set<String> headerKeySet = headers.keySet();

        for (String headerName : headerKeySet) {
            Log.d(TAG, "[ " + headerName + " : " + headers.get(headerName)
                    + " ]");
        }
    }

    private void logParams(Map<String, String> params) {
        Set<String> paramsKeySet = params.keySet();

        for (String paramName : paramsKeySet) {
            Log.d(TAG, "[ " + paramName + " : " + params.get(paramName)
                    + " ]");
        }
    }


    /**
     * Used to get current date and time
     * @return
     */
    private String currentDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

}
