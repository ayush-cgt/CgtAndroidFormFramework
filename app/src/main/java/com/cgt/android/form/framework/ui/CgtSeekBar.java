package com.cgt.android.form.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.cgt.android.form.framework.R;

/**
 * Created by kst-android on 22/10/15.
 */
public class CgtSeekBar extends SeekBar {

    String serverParamKey = null;
    String validationMessage = null;

    public CgtSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CgtView);

            String propertyValue = null;

            propertyValue = a.getString(R.styleable.CgtView_serverParamKey);
            if (propertyValue != null) {
                setServerParamKey(propertyValue);
            }

            a.recycle();
        }
    }

    private void setServerParamKey(String keyName) {

        this.serverParamKey = keyName;
    }

    public String getServerParamKey() {

        return this.serverParamKey;
    }
}
