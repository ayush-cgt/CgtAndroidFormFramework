package com.cgt.android.form.framework.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by kst-android on 5/10/15.
 */
public class KeyboardUtil {

    public static void showKeyboard(Context context, View textView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            textView.requestFocus();
            imm.showSoftInput(textView, 0);
        }
    }

    public static void showKeyboard(Context context, EditText textView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            textView.requestFocus();
            textView.selectAll();
            imm.showSoftInput(textView, 0);
        }
    }

    public static void hideKeyboard(Context context, View textView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
    }
}
