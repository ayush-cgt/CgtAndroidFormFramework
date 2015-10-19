package com.cgt.android.form.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.cgt.android.form.framework.R;

/**
 * Created by Ayush Verma on 22-09-2015.
 */
public class CgtEditText extends EditText {

    String serverParamKey = null;
    String validationMessage = null;


    public CgtEditText(Context context) {
        super(context);
    }

    public CgtEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CgtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CgtView);

            String propertyValue = null;
            propertyValue = a.getString(R.styleable.CgtView_fontName);
            if (propertyValue != null) {
                setFont(propertyValue);
            }

            propertyValue = a.getString(R.styleable.CgtView_serverParamKey);
            if (propertyValue != null) {
                setServerParamKey(propertyValue);
            }

            propertyValue = a.getString(R.styleable.CgtView_validationMessage);
            if (propertyValue != null) {
                setValidationMessage(propertyValue);
            }

            a.recycle();
        }
    }

    private void setFont(String fontName) {

        if (fontName != null) {
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
            setTypeface(myTypeface);
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


}
