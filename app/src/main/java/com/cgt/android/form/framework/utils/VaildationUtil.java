package com.cgt.android.form.framework.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kst-android on 19/10/15.
 */
public class VaildationUtil {

    public static boolean isEmailValid(String email)
    {
        boolean isValid = false;

        String expression = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPasswordValid(String psw)
    {
        boolean isValid = false;

        String expression = "^.{4,8}$";
        CharSequence inputStr = psw;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isConfirmPasswordValid(String oldPsw, String psw)
    {
        try {
            if (!TextUtils.isEmpty(psw) && oldPsw.equals(psw)) {
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    public static boolean isRangeValid(int min, int max, String text)
    {
        try {
            int value = Integer.parseInt(text);
            if ((value > min) && (value < max)) {
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    public static boolean isPhoneValid(String phone)
    {
        boolean isValid = false;

        String expression = "^[+]?[0-9]{10,13}$";
        CharSequence inputStr = phone;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidCardNumber(String cardNumber)
    {
        boolean isValid = false;

        String expression = "^.{12,16}$";
        CharSequence inputStr = cardNumber;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isUserNameValid(String username)
    {
        boolean isValid = false;

        String expression = "^[a-zA-Z0-9_-]{5,10}$";
        CharSequence inputStr = username;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

}
