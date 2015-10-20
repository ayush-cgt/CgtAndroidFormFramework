package com.cgt.android.form.framework.web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.cgt.android.form.framework.R;
import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.models.Model;
import com.cgt.android.form.framework.utils.Utilities;

/**
 * Created by kst-android on 20/10/15.
 */
public class WebserviceTask extends AsyncTask<String, Void, String> {


    private Activity mActivity;
    IOnServerResponse serverResponseListener;
    ProgressDialog progressDialog;


    private boolean isSuccess = false;
    private String Url;
    private String response;
    private String jsonText;
    private Model responseModel;

    public WebserviceTask(Activity mActivity, String Url, IOnServerResponse serverResponseListener) {
        this.mActivity = mActivity;
        this.Url = Url;
        this.serverResponseListener = serverResponseListener;
    }

    public void addPostJson(String jsonText) {
        this.jsonText = jsonText;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(mActivity.getString(R.string.please_wait));
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            if (Utilities.checkNetworkConnection(mActivity)) {

                ServerClient serverClient = new ServerClient(mActivity, Url);
                response = serverClient.executePostRequest(jsonText);
                isSuccess = serverClient.isSuccess();

            }
        } catch (Exception e) {

        }

        return response;
    }

    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        if (isSuccess) {
            serverResponseListener.onServerSuccess(responseModel);
        } else {
            serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
        }
    }
}