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

import com.cgt.android.form.framework.R;
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

    //private CommonListener listener;

    private Activity mActivity;
    ArrayList<View> arrayViews = new ArrayList<View>();
    private View rootView = null;

    public CommonUtil(Activity activity) {
        this.mActivity = activity;

        rootView = this.mActivity.getWindow().getDecorView().getRootView();
        arrayViews = getAllChildren(rootView);

        Log.d("Views count", arrayViews.size() + "");
    }

    // Assign the listener implementing events interface that will receive the events
    /*public void setCommonListener(CommonListener listener) {
        this.listener = listener;
    }*/

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
        } else if (vg instanceof RadioGroup) {
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

            for (int i = 0; i < arrayViews.size(); i++) {
                View view = arrayViews.get(i);
                if (view instanceof CgtEditText) {
                    CgtEditText field = (CgtEditText) view;

                    System.out.println("server_key >>" + field.getServerParamKey());

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                if (!TextUtils.isEmpty(field.getValidationMessage())) {
                                    Toast.makeText(mActivity, field.getValidationMessage(), Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_empty_field), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }

                        if (field.isEmail()) { // is valid email
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                if (!TextUtils.isEmpty(field.getValidationMessage())) {
                                    Toast.makeText(mActivity, field.getValidationMessage(), Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_empty_field), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else if (!VaildationUtil.isEmailValid(field.getText().toString())) {
                                Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_invalid_email), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        if (field.isPassword()) { // is valid password
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                if (!TextUtils.isEmpty(field.getValidationMessage())) {
                                    Toast.makeText(mActivity, field.getValidationMessage(), Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_empty_field), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else if (!VaildationUtil.isPasswordValid(field.getText().toString())) {
                                Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_invalid_password), Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (field.getComparePassword() != -1) { // reference added
                                CgtEditText fieldPsw = (CgtEditText) rootView.findViewById(field.getComparePassword());
                                if (!VaildationUtil.isConfirmPasswordValid(field.getText().toString(), fieldPsw.getText().toString())) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_invalid_confirm_password), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else { // reference not added
                                if (field.getServerParamKey().equals("nil")) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_compare_password_resource_missing), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }

                        if (!field.getServerParamKey().equals("nil"))
                            jsonObject.put(field.getServerParamKey(), field.getText().toString());
                    }
                }
            }
            System.out.println("Json >> " + jsonObject.toString());
            //new postAsync().execute(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class postAsync extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                if (Utilities.checkNetworkConnection(mActivity)) {
                    //String json = urls[0];
                    //String response = post("http://www.roundsapp.com/post", json);
                    response = post("https://raw.githubusercontent.com/square/okhttp/master/README.md", urls[0]);
                } else {
                    //listener.onError(mActivity.getString(R.string.error_network));
                }
            } catch (Exception e) {

            }
            return urls[0];
        }

        protected void onPostExecute(String result) {
                /*try {
                    listener.onSuccess(new JSONObject(result));
                } catch (JSONException e) {
                    listener.onError(mActivity.getString(R.string.error_json_not_valid));
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
        } catch (IOException e) {
            return null;
        }
    }
}
