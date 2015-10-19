package com.cgt.android.form.framework.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.cgt.android.form.framework.ui.CgtEditText;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kst-android on 19/10/15.
 */
public class CommonUtil {

    private Activity mActivity;
    ArrayList<View> arrayViews = new ArrayList<View>();

    public CommonUtil(Activity activity) {
        this.mActivity = activity;

        arrayViews = getAllChildren(this.mActivity.getWindow().getDecorView().getRootView());

        Log.d("Views count", arrayViews.size() + "");
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;

        if (vg instanceof Spinner) {
            result.add(vg);
        }
        else if (vg instanceof RadioGroup) {
            result.add(vg);
        }

        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }

        return result;
    }

    public void submitFormData() {
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i<arrayViews.size(); i ++) {
                View view = arrayViews.get(i);
                if (view instanceof CgtEditText) {
                    CgtEditText field = (CgtEditText) view;

                    if(!TextUtils.isEmpty(field.getServerParamKey())) { // server key
                        if (TextUtils.isEmpty(field.getText().toString())) { // text empty
                            if (TextUtils.isEmpty(field.getValidationMessage())) { // validation message empty
                                jsonObject.put(field.getServerParamKey(), field.getText().toString());
                                break;
                            } else { //
                                Toast.makeText(mActivity, field.getValidationMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }



            new postAsync().execute(jsonObject.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class postAsync extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                if(Utilities.checkNetworkConnection(mActivity))
                {
                    //String json = urls[0];
                    //String response = post("http://www.roundsapp.com/post", json);
                    response = post("https://raw.githubusercontent.com/square/okhttp/master/README.md", urls[0]);
                }
                else {
                    //listener.onError(mActivity.getString(R.string.error));
                }
            } catch (Exception e) {

            }
            return urls[0];
        }

        protected void onPostExecute(String result) {
                /*try {
                    listener.onSuccess(new JSONObject(result));
                } catch (JSONException e) {
                    listener.onError(mActivity.getString(R.string.error));
                }*/
        }
    }

    private String post(String url, String json) {
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (IOException e) {
            return  null;
        }
    }
}
