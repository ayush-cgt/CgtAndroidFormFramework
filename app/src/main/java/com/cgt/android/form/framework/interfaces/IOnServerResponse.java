package com.cgt.android.form.framework.interfaces;

import com.cgt.android.form.framework.models.Model;

/**
 * Created by kst-android on 20/10/15.
 */
public interface IOnServerResponse {

    public void onServerSuccess(Model responseData);

    public void onServerFailure(Model responseData, String failureText);

}
