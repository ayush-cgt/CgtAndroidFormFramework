package com.cgt.android.form.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cgt.android.form.framework.R;

/**
 * Created by kst-android on 21/10/15.
 */
public class CgtImageView extends ImageView {

    String serverParamKey = null;
    String filePath = null;

    public CgtImageView(Context context, AttributeSet attrs) {
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

            propertyValue = a.getString(R.styleable.CgtView_fileUri);
            if (propertyValue != null) {
                setFilePath(propertyValue);
            }

            a.recycle();
        }
    }

    private void setServerParamKey(String keyName) {

        this.serverParamKey = keyName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getServerParamKey() {

        return this.serverParamKey;
    }

    public String getFilePath() {

        return this.filePath;
    }

}

