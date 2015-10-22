package com.cgt.android.form.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RatingBar;

import com.cgt.android.form.framework.R;

/**
 * Created by kst-android on 22/10/15.
 */
public class CgtRatingBar extends RatingBar {

    String serverParamKey = null;

    public CgtRatingBar(Context context, AttributeSet attrs) {
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
