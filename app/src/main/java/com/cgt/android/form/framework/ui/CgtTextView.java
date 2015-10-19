package com.cgt.android.form.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cgt.android.form.framework.R;

/**
 * Created by Ayush Verma on 21-09-2015.
 */
public class CgtTextView extends TextView {


    public CgtTextView(Context context) {
        super(context);
    }

    public CgtTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CgtTextView(Context context, AttributeSet attrs) {
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
