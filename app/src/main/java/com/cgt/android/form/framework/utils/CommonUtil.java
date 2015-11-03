package com.cgt.android.form.framework.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cgt.android.form.framework.R;
import com.cgt.android.form.framework.configurations.PhotoConfig;
import com.cgt.android.form.framework.imageloader.PicassoImageLoader;
import com.cgt.android.form.framework.interfaces.IOnGenericValidation;
import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.photo.PhotoHandler;
import com.cgt.android.form.framework.ui.CgtEditText;
import com.cgt.android.form.framework.ui.CgtImageView;
import com.cgt.android.form.framework.ui.CgtRadioGroup;
import com.cgt.android.form.framework.ui.CgtRatingBar;
import com.cgt.android.form.framework.ui.CgtSeekBar;
import com.cgt.android.form.framework.ui.CgtSpinner;
import com.cgt.android.form.framework.ui.CgtSwitch;
import com.cgt.android.form.framework.web.ParseClient;
import com.cgt.android.form.framework.web.WebserviceTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kst-android on 19/10/15.
 */
public class CommonUtil {

    private Activity mActivity;
    ArrayList<View> arrayViews = new ArrayList<View>();
    private View rootView = null;
    private String jsonObjectString = "";
    private HashMap<String, String> mapServerParam = new HashMap<String, String>();

    PhotoHandler photoHandler;

    public CommonUtil(Activity activity) {
        this.mActivity = activity;
        photoHandler = new PhotoHandler(mActivity);

        rootView = this.mActivity.getWindow().getDecorView().getRootView();
        arrayViews = getAllChildren(rootView);

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

        if (vg instanceof CgtSpinner) {
            result.add(vg);
        } else if (vg instanceof CgtRadioGroup) {
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

    /*public void submitFormData(IOnGenericValidation onGenericValidation, IOnServerResponse serverResponseListener, String targetUrl) {
        JSONObject jsonObject = new JSONObject();
        try {

            for (int i = 0; i < arrayViews.size(); i++) {
                View view = arrayViews.get(i);

                if (view instanceof CgtEditText) {
                    CgtEditText field = (CgtEditText) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return;
                            }
                        }

                        if (field.isEmail()) { // is valid email
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return;
                            } else if (!VaildationUtil.isEmailValid(field.getText().toString())) {
                                displayMessage(field, mActivity.getString(R.string.alert_general_invalid_email));
                                return;
                            }
                        }

                        if (field.isPassword()) { // is valid password
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return;
                            } else if (!VaildationUtil.isPasswordValid(field.getText().toString())) {
                                displayMessage(field, mActivity.getString(R.string.alert_general_invalid_password));
                                return;
                            }

                            if (field.getComparePassword() != -1) { // reference added
                                CgtEditText fieldPsw = (CgtEditText) rootView.findViewById(field.getComparePassword());
                                if (!VaildationUtil.isConfirmPasswordValid(field.getText().toString(), fieldPsw.getText().toString())) {
                                    displayMessage(field, mActivity.getString(R.string.alert_general_invalid_confirm_password));
                                    return;
                                }
                            } else { // reference not added
                                if (field.getServerParamKey().equals("nil")) {
                                    displayMessage(field, mActivity.getString(R.string.alert_general_compare_password_resource_missing));
                                    return;
                                }
                            }
                        }

                        if (onGenericValidation != null) {
                            if (!onGenericValidation.onGenricValidated()) {
                                return;
                            }
                        }

                        if (!field.getServerParamKey().equals("nil"))
                            jsonObject.put(field.getServerParamKey(), field.getText().toString());
                    }
                } else if (view instanceof CgtSpinner) {
                    CgtSpinner field = (CgtSpinner) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (field.getSelectedItemPosition() == 0) {
                                displayNotEmptyMessage(field);
                                return;
                            }
                        }

                        if (field.getSelectedItemPosition() != 0)
                            jsonObject.put(field.getServerParamKey(), field.getSelectedItem().toString());
                        else
                            jsonObject.put(field.getServerParamKey(), "");
                    }
                } else if (view instanceof CgtRadioGroup) {
                    CgtRadioGroup field = (CgtRadioGroup) view;

                    int radioButtonID = field.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) field.findViewById(radioButtonID);

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (radioButton == null || TextUtils.isEmpty(radioButton.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return;
                            }
                        }

                        if (radioButton != null)
                            jsonObject.put(field.getServerParamKey(), radioButton.getText().toString());
                        else {
                            jsonObject.put(field.getServerParamKey(), "");
                        }
                    }
                } else if (view instanceof CgtSeekBar) {
                    CgtSeekBar field = (CgtSeekBar) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        jsonObject.put(field.getServerParamKey(), field.getProgress());
                    }

                } else if (view instanceof CgtRatingBar) {
                    CgtRatingBar field = (CgtRatingBar) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        jsonObject.put(field.getServerParamKey(), field.getRating());
                    }
                } else if (view instanceof CgtSwitch) {
                    CgtSwitch field = (CgtSwitch) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        jsonObject.put(field.getServerParamKey(), field.isChecked());
                    }
                } else if (view instanceof ImageView) {
                    ImageView field = (ImageView) view;

                }
            }

            if (arrayViews.size() > 0) {
                KeyboardUtil.hideKeyboard(mActivity, arrayViews.get(0));
            }

            System.out.println("Json >> " + jsonObject.toString());
            interactServerToPostData(serverResponseListener, jsonObject.toString(), targetUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public boolean isValid(IOnGenericValidation onGenericValidation) {
        JSONObject jsonObject = new JSONObject();
        try {

            for (int i = 0; i < arrayViews.size(); i++) {
                View view = arrayViews.get(i);

                if (view instanceof CgtEditText) {
                    CgtEditText field = (CgtEditText) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return false;
                            }
                        }

                        if (field.isEmail()) { // is valid email
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return false;
                            } else if (!VaildationUtil.isEmailValid(field.getText().toString())) {
                                displayMessage(field, mActivity.getString(R.string.alert_general_invalid_email));
                                return false;
                            }
                        }

                        if (field.isPassword()) { // is valid password
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return false;
                            } else if (!VaildationUtil.isPasswordValid(field.getText().toString())) {
                                displayMessage(field, mActivity.getString(R.string.alert_general_invalid_password));
                                return false;
                            }

                            if (field.getComparePassword() != -1) { // reference added
                                CgtEditText fieldPsw = (CgtEditText) rootView.findViewById(field.getComparePassword());
                                if (!VaildationUtil.isConfirmPasswordValid(field.getText().toString(), fieldPsw.getText().toString())) {
                                    displayMessage(field, mActivity.getString(R.string.alert_general_invalid_confirm_password));
                                    return false;
                                }
                            } else { // reference not added
                                if (field.getServerParamKey().equals("nil")) {
                                    displayMessage(field, mActivity.getString(R.string.alert_general_compare_password_resource_missing));
                                    return false;
                                }
                            }
                        }

                        if (onGenericValidation != null) {
                            if (!onGenericValidation.onGenricValidated()) {
                                return false;
                            }
                        }

                        if (!field.getServerParamKey().equals("nil")) {
                            jsonObject.put(field.getServerParamKey(), field.getText().toString());
                            mapServerParam.put(field.getServerParamKey(), field.getText().toString());
                        }
                    }
                } else if (view instanceof CgtSpinner) {
                    CgtSpinner field = (CgtSpinner) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (field.getSelectedItemPosition() == 0) {
                                displayNotEmptyMessage(field);
                                return false;
                            }
                        }

                        if (field.getSelectedItemPosition() != 0) {
                            jsonObject.put(field.getServerParamKey(), field.getSelectedItem().toString());
                            mapServerParam.put(field.getServerParamKey(), field.getSelectedItem().toString());
                        }
                        else{
                            jsonObject.put(field.getServerParamKey(), "");
                            mapServerParam.put(field.getServerParamKey(), "");
                        }
                    }
                } else if (view instanceof CgtRadioGroup) {
                    CgtRadioGroup field = (CgtRadioGroup) view;

                    int radioButtonID = field.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) field.findViewById(radioButtonID);

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (radioButton == null || TextUtils.isEmpty(radioButton.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return false;
                            }
                        }

                        if (radioButton != null) {
                            jsonObject.put(field.getServerParamKey(), radioButton.getText().toString());
                            mapServerParam.put(field.getServerParamKey(), radioButton.getText().toString());
                        }
                        else {
                            jsonObject.put(field.getServerParamKey(), "");
                            mapServerParam.put(field.getServerParamKey(), "");
                        }
                    }
                } else if (view instanceof CgtSeekBar) {
                    CgtSeekBar field = (CgtSeekBar) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        jsonObject.put(field.getServerParamKey(), field.getProgress());
                        mapServerParam.put(field.getServerParamKey(), "" + field.getProgress());
                    }

                } else if (view instanceof CgtRatingBar) {
                    CgtRatingBar field = (CgtRatingBar) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        jsonObject.put(field.getServerParamKey(), field.getRating());
                        mapServerParam.put(field.getServerParamKey(), "" + field.getRating());
                    }
                } else if (view instanceof CgtSwitch) {
                    CgtSwitch field = (CgtSwitch) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        jsonObject.put(field.getServerParamKey(), field.isChecked());
                        mapServerParam.put(field.getServerParamKey(), "" + field.isChecked());
                    }
                } else if (view instanceof ImageView) {
                    ImageView field = (ImageView) view;

                    /*if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        jsonObject.put(field.getServerParamKey(), field.get);
                    }*/
                }
            }

            if (arrayViews.size() > 0) {
                KeyboardUtil.hideKeyboard(mActivity, arrayViews.get(0));
            }

            jsonObjectString = jsonObject.toString();
            System.out.println("Json >> " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void postParseApiResponse(IOnServerResponse serverResponseListener, String tableName) {
        ParseClient parsePostTask = new ParseClient(mActivity, serverResponseListener);
        //parsePostTask.postResponse(tableName, columnName, jsonObjectString);
        parsePostTask.postResponse(tableName, mapServerParam);
    }

    public void postResponse(IOnServerResponse serverResponseListener, String targetUrl) {
        interactServerToPostData(serverResponseListener, jsonObjectString, targetUrl);
    }

    public void getParseApiResponse(IOnServerResponse serverResponseListener, String tableName, String whereClause) {
        ParseClient parseGetTask = new ParseClient(mActivity, serverResponseListener);
        parseGetTask.getResponse(tableName);
    }

    public void getResponse(IOnServerResponse serverResponseListener, String targetUrl) {
        interactServerToGetData(serverResponseListener, targetUrl);
    }

    void interactServerToPostData(IOnServerResponse serverResponseListener, String jsonText, String targetUrl) {
        if (serverResponseListener != null) {
            WebserviceTask serverTask = new WebserviceTask(mActivity, targetUrl, serverResponseListener);
            serverTask.addPostJson(jsonText);
            serverTask.execute();
        }
    }

    void interactServerToGetData(IOnServerResponse serverResponseListener, String targetUrl) {
        if (serverResponseListener != null) {
            WebserviceTask serverTask = new WebserviceTask(mActivity, targetUrl, serverResponseListener);
            serverTask.execute();
        }
    }

    public void bindLayoutFromResponse(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            for (int i = 0; i < arrayViews.size(); i++) {
                View view = arrayViews.get(i);

                if (view instanceof CgtEditText) {
                    CgtEditText field = (CgtEditText) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (!field.getServerParamKey().equals("nil")) {
                            if (jsonObject.has(field.getServerParamKey())) {
                                field.setText(jsonObject.getString(field.getServerParamKey()));
                            }
                        }
                    }
                } else if (view instanceof CgtSpinner) {
                    CgtSpinner field = (CgtSpinner) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (jsonObject.has(field.getServerParamKey())) {
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>)field.getAdapter();
                            field.setSelection(adapter.getPosition(jsonObject.getString(field.getServerParamKey())));
                        }
                    }
                } else if (view instanceof CgtRadioGroup) {
                    CgtRadioGroup field = (CgtRadioGroup) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        for (int k=0; k<field.getChildCount(); k++) {
                            RadioButton radioButton = (RadioButton) field.getChildAt(k);

                            if (radioButton.getText().equals(jsonObject.getString(field.getServerParamKey()))) {
                                radioButton.setChecked(true);
                            }
                        }
                    }
                } else if (view instanceof CgtSeekBar) {
                    CgtSeekBar field = (CgtSeekBar) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (jsonObject.has(field.getServerParamKey())) {
                            field.setProgress(Integer.parseInt(jsonObject.getString(field.getServerParamKey())));
                        }
                    }
                } else if (view instanceof CgtRatingBar) {
                    CgtRatingBar field = (CgtRatingBar) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (jsonObject.has(field.getServerParamKey())) {
                            field.setRating(Float.parseFloat(jsonObject.getString(field.getServerParamKey())));
                        }
                    }
                } else if (view instanceof CgtSwitch) {
                    CgtSwitch field = (CgtSwitch) view;

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (jsonObject.has(field.getServerParamKey())) {
                            field.setChecked(Boolean.parseBoolean(jsonObject.getString(field.getServerParamKey())));
                        }
                    }
                } else if (view instanceof ImageView) {
                    ImageView field = (ImageView) view;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void captureCameraImage(CgtImageView imageView) {
        if (photoHandler.checkCameraHardware())
            photoHandler.captureImage(imageView, PhotoConfig.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void captureGalleryImage(CgtImageView imageView) {
        if (photoHandler.checkCameraHardware())
            photoHandler.captureImage(imageView, PhotoConfig.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoHandler.onActivityResult(requestCode, resultCode, data);
    }

    public void displayImage(String imageUrl, ImageView imageView) {
        PicassoImageLoader.displayImage(mActivity, imageUrl, imageView);
    }

    public void displayImage(String imageUrl, ImageView imageView, int defaultImageResId) {
        PicassoImageLoader.displayImage(mActivity, imageUrl, imageView, defaultImageResId);
    }

    public void displayImage(String imageUrl, ImageView imageView, int defaultImageResId, int errorImageResId) {
        PicassoImageLoader.displayImage(mActivity, imageUrl, imageView, defaultImageResId, errorImageResId);
    }

    public void displayImage(String imageUrl, ImageView imageView, int defaultImageResId, int errorImageResId, int imgWidth, int imgHeight) {
        PicassoImageLoader.displayImage(mActivity, imageUrl, imageView, defaultImageResId, errorImageResId, imgWidth, imgHeight);
    }

    public void displayImage(String imageUrl, ImageView imageView, int defaultImageResId, int errorImageResId, int imgWidth, int imgHeight, float rotation) {
        PicassoImageLoader.displayImage(mActivity, imageUrl, imageView, defaultImageResId, errorImageResId, imgWidth, imgHeight, rotation);
    }


    private void displayNotEmptyMessage(CgtEditText field) {
        if (!TextUtils.isEmpty(field.getValidationMessage())) {
            displayMessage(field, field.getValidationMessage());
        } else {
            displayMessage(field, mActivity.getString(R.string.alert_general_empty_field));
        }
    }

    private void displayNotEmptyMessage(CgtSpinner field) {
        if (!TextUtils.isEmpty(field.getValidationMessage())) {
            displayMessage(field, field.getValidationMessage());
        } else {
            displayMessage(field, mActivity.getString(R.string.alert_general_empty_field));
        }
    }

    private void displayNotEmptyMessage(CgtRadioGroup field) {
        if (!TextUtils.isEmpty(field.getValidationMessage())) {
            displayMessage(field, field.getValidationMessage());
        } else {
            displayMessage(field, mActivity.getString(R.string.alert_general_empty_field));
        }
    }

    private void displayMessage(CgtEditText field, String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        KeyboardUtil.showKeyboard(mActivity, field);
    }

    private void displayMessage(CgtSpinner field, String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        KeyboardUtil.showKeyboard(mActivity, field);
    }

    private void displayMessage(CgtRadioGroup field, String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        KeyboardUtil.showKeyboard(mActivity, field);
    }

}
