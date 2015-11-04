package com.cgt.android.form.framework.web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cgt.android.form.framework.R;
import com.cgt.android.form.framework.configurations.ResponseCodes;
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
    private String filePath;
    private Model responseModel;

    public WebserviceTask(Activity mActivity, String Url, IOnServerResponse serverResponseListener) {
        this.mActivity = mActivity;
        this.Url = Url;
        this.serverResponseListener = serverResponseListener;
    }

    public void addPostJson(String jsonText) {
        this.jsonText = jsonText;
    }

    public void addPostImagePath(String filePath) {
        this.filePath = filePath;
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

                if (!TextUtils.isEmpty(jsonText) && !TextUtils.isEmpty(filePath)) {
                    response = serverClient.executePostMultiPartRequest(jsonText, filePath);
                } else if (!TextUtils.isEmpty(jsonText)) {
                    response = serverClient.executePostRequest(jsonText);
                } else if (!TextUtils.isEmpty(filePath)) {
                    response = serverClient.executePostMultiPartRequest(jsonText, filePath);
                } else {
                    response = serverClient.executeGetRequest();
                }

                isSuccess = serverClient.isSuccess();
            }
        } catch (Exception e) {

        }

        return response;
    }

    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        responseModel = new Model();
        if (isSuccess) {
            responseModel.responseCode = ResponseCodes.RESPONSE_SUCCESS;
            responseModel.responseMessage = result;
            serverResponseListener.onServerSuccess(responseModel);
        } else {
            responseModel.responseCode = ResponseCodes.RESPONSE_FAILURE;
            responseModel.responseMessage = result;
            serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
        }
    }
}