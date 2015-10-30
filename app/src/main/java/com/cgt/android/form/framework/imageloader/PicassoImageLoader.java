package com.cgt.android.form.framework.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by kst-android on 29/10/15.
 */
public class PicassoImageLoader {

    public static void displayImage(Context mContext, String imageUrl, ImageView imageView) {
        Picasso.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }

    public static void displayImage(Context mContext, String imageUrl, ImageView imageView, int defaultImageResId) {
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(defaultImageResId)
                .into(imageView);
    }

    public static void displayImage(Context mContext, String imageUrl, ImageView imageView, int defaultImageResId, int errorImageResId) {
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(defaultImageResId)
                .error(errorImageResId)
                .into(imageView);
    }

    public static void displayImage(Context mContext, String imageUrl, ImageView imageView, int defaultImageResId, int errorImageResId, int imgWidth, int imgHeight) {
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(defaultImageResId)
                .error(errorImageResId)
                .resize(imgWidth, imgHeight)
                .into(imageView);
    }

    public static void displayImage(Context mContext, String imageUrl, ImageView imageView, int defaultImageResId, int errorImageResId, int imgWidth, int imgHeight, float rotation) {
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(defaultImageResId)
                .error(errorImageResId)
                .resize(imgWidth, imgHeight)
                .rotate(rotation)
                .into(imageView);
    }
}
