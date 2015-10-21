package com.cgt.android.form.framework.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cgt.android.form.framework.R;
import com.cgt.android.form.framework.interfaces.IOnGenericValidation;
import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.ui.CgtEditText;
import com.cgt.android.form.framework.ui.CgtRadioGroup;
import com.cgt.android.form.framework.ui.CgtSpinner;
import com.cgt.android.form.framework.web.WebserviceTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kst-android on 19/10/15.
 */
public class CommonUtil {

    private Activity mActivity;
    ArrayList<View> arrayViews = new ArrayList<View>();
    private View rootView = null;

    public CommonUtil(Activity activity) {
        this.mActivity = activity;

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

    public void submitFormData(IOnGenericValidation onGenericValidation, IOnServerResponse serverResponseListener, String targetUrl) {
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

                    System.out.println("server_key >>" + field.getServerParamKey());

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (field.getSelectedItemPosition() == 0) {
                                displayNotEmptyMessage(field);
                                return;
                            }
                        }

                        if (!field.getServerParamKey().equals("nil"))
                            jsonObject.put(field.getServerParamKey(), field.getSelectedItem().toString());
                    }
                } else if (view instanceof CgtRadioGroup) {
                    CgtRadioGroup field = (CgtRadioGroup) view;

                    System.out.println("server_key >>" + field.getServerParamKey());

                    int radioButtonID = field.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) field.findViewById(radioButtonID);

                    if (!TextUtils.isEmpty(field.getServerParamKey())) { // check server key
                        if (field.isCompulsory()) { // is compulsory
                            if (radioButton == null || TextUtils.isEmpty(radioButton.getText().toString())) {
                                displayNotEmptyMessage(field);
                                return;
                            }
                        }

                        if (radioButton != null && !field.getServerParamKey().equals("nil"))
                            jsonObject.put(field.getServerParamKey(), radioButton.getText().toString());
                    }
                }
            }

            if (arrayViews.size()>0) {
                KeyboardUtil.hideKeyboard(mActivity, arrayViews.get(0));
            }

            System.out.println("Json >> " + jsonObject.toString());
            interactServerToPostData(serverResponseListener, jsonObject.toString(), targetUrl);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void interactServerToPostData(IOnServerResponse serverResponseListener, String jsonText, String targetUrl) {
        if (serverResponseListener != null) {
            WebserviceTask serverTask = new WebserviceTask(mActivity, targetUrl, serverResponseListener);
            serverTask.addPostJson(jsonText);
            serverTask.execute();
        }
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
