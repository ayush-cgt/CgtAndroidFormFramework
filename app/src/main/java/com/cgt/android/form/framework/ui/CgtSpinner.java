package com.cgt.android.form.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Spinner;

import com.cgt.android.form.framework.R;

/**
 * Created by kst-android on 21/10/15.
 */
public class CgtSpinner extends Spinner {

    String serverParamKey = null;
    String validationMessage = null;
    boolean isCompulsory = false;

    public CgtSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CgtView);

            String propertyValue = null;

            propertyValue = a.getString(R.styleable.CgtView_serverParamKey);
            if (propertyValue != null) {
                setServerParamKey(propertyValue);
            }

            propertyValue = a.getString(R.styleable.CgtView_validationMessage);
            if (propertyValue != null) {
                setValidationMessage(propertyValue);
            }

            isCompulsory = a.getBoolean(R.styleable.CgtView_isCompulsory, false);

            a.recycle();
        }
    }

    private void setServerParamKey(String keyName) {

        this.serverParamKey = keyName;
    }

    private void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getServerParamKey() {

        return this.serverParamKey;
    }

    public String getValidationMessage() {

        return this.validationMessage;
    }

    public boolean isCompulsory() {
        return isCompulsory;
    }

}
