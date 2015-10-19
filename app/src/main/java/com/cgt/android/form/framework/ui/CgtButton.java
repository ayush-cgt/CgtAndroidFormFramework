package com.cgt.android.form.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.cgt.android.form.framework.R;


/**
 * Created by Ayush Verma on 22-09-2015.
 */
public class CgtButton extends Button {


    public CgtButton(Context context) {
        super(context);
    }

    public CgtButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CgtButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CgtView);
            String propertyValue = a.getString(R.styleable.CgtView_fontName);
            if (propertyValue != null) {
                setFont(propertyValue);
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


}
