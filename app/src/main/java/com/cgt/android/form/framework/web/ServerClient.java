package com.cgt.android.form.framework.web;

import android.content.Context;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by kst-android on 20/10/15.
 */
public class ServerClient {

    Context mContext;
    OkHttpClient client;
    String serverUrl;

    Request request;
    Response response;


    public ServerClient(Context mContext, String serverUrl) {
        this.mContext = mContext;
        this.client = new OkHttpClient();
        this.serverUrl = serverUrl;
    }

    public boolean isSuccess() {

        if (response != null) {
            return response.isSuccessful();
        }

        return false;
    }

    public String executePostRequest(String jsonText) throws IOException {

        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonText);
            request = new Request.Builder()
                    .url(serverUrl)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.body().string();
    }

    public String executeGetRequest() throws IOException {
        try {
            request = new Request.Builder()
                    .url(serverUrl)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.body().string();
    }

}
