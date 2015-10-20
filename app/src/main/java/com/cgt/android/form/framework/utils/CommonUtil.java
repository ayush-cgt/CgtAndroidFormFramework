package com.cgt.android.form.framework.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.cgt.android.form.framework.R;
import com.cgt.android.form.framework.interfaces.IOnGenericValidation;
import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.ui.CgtEditText;
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
                                if (!TextUtils.isEmpty(field.getValidationMessage())) {
                                    Toast.makeText(mActivity, field.getValidationMessage(), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
                                    return;
                                } else {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_empty_field), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
                                    return;
                                }
                            }
                        }

                        if (field.isEmail()) { // is valid email
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                if (!TextUtils.isEmpty(field.getValidationMessage())) {
                                    Toast.makeText(mActivity, field.getValidationMessage(), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
                                    return;
                                } else {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_empty_field), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
                                    return;
                                }
                            } else if (!VaildationUtil.isEmailValid(field.getText().toString())) {
                                Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_invalid_email), Toast.LENGTH_LONG).show();
                                KeyboardUtil.showKeyboard(mActivity, field);
                                return;
                            }
                        }

                        if (field.isPassword()) { // is valid password
                            if (TextUtils.isEmpty(field.getText().toString())) {
                                if (!TextUtils.isEmpty(field.getValidationMessage())) {
                                    Toast.makeText(mActivity, field.getValidationMessage(), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
                                    return;
                                } else {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_empty_field), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
                                    return;
                                }
                            } else if (!VaildationUtil.isPasswordValid(field.getText().toString())) {
                                Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_invalid_password), Toast.LENGTH_LONG).show();
                                KeyboardUtil.showKeyboard(mActivity, field);
                                return;
                            }

                            if (field.getComparePassword() != -1) { // reference added
                                CgtEditText fieldPsw = (CgtEditText) rootView.findViewById(field.getComparePassword());
                                if (!VaildationUtil.isConfirmPasswordValid(field.getText().toString(), fieldPsw.getText().toString())) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_invalid_confirm_password), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
                                    return;
                                }
                            } else { // reference not added
                                if (field.getServerParamKey().equals("nil")) {
                                    Toast.makeText(mActivity, mActivity.getString(R.string.alert_general_compare_password_resource_missing), Toast.LENGTH_LONG).show();
                                    KeyboardUtil.showKeyboard(mActivity, field);
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
}
