package com.cgt.android.form.framework.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.cgt.android.form.framework.configurations.PhotoConfig;
import com.cgt.android.form.framework.ui.CgtImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kst-android on 2/11/15.
 */
public class PhotoHandler {

    private final Activity mContext;
    private CgtImageView mImageView;

    private Uri fileUri;
    Bitmap bitmap = null;
    String filePath = null;

    public PhotoHandler(Activity context) {
        this.mContext = context;
    }

    public void captureImage(CgtImageView imageView, int requestCode) {
        this.mImageView = imageView;

        switch (requestCode) {
            case PhotoConfig.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                // create Intent to take a picture and return control to the calling application
                Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(); // create a file to save the image
                in.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                mContext.startActivityForResult(in, requestCode);
                break;
            case PhotoConfig.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mContext.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
                break;
            default:
                Toast.makeText(mContext.getApplicationContext(), "Camera/Gallery request code not valid", Toast.LENGTH_LONG).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case PhotoConfig.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    try
                    {
                        // OI FILE Manager
                        String filemanagerstring = fileUri.getPath();

                        // MEDIA GALLERY
                        String selectedImagePath = getPath(fileUri);

                        if (selectedImagePath != null)
                        {
                            filePath = selectedImagePath;
                        }
                        else if (filemanagerstring != null)
                        {
                            filePath = filemanagerstring;
                        }
                        else
                        {
                            Toast.makeText(mContext.getApplicationContext(), "Unknown path", Toast.LENGTH_LONG).show();
                            Log.e("Bitmap", "Unknown path");
                        }

                        if (filePath != null)
                        {
                            String imgLink = filePath;
                            bitmap = decodeFile(imgLink);

                            mImageView.setImageBitmap(bitmap);
                            mImageView.setFilePath(filePath);
                        }
                        else
                        {
                            bitmap = null;
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(mContext.getApplicationContext(), "Internal error", Toast.LENGTH_LONG).show();
                        Log.e(e.getClass().getName(), e.getMessage(), e);
                    }
                } 
            }
            break;
            case PhotoConfig.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri selectedImageUri = data.getData();
                    if(data.getData()!=null)
                    {
                        try
                        {
                            // OI FILE Manager
                            String filemanagerstring = selectedImageUri.getPath();

                            // MEDIA GALLERY
                            String selectedImagePath = getPath(selectedImageUri);

                            if (selectedImagePath != null)
                            {
                                filePath = selectedImagePath;
                            }
                            else if (filemanagerstring != null)
                            {
                                filePath = filemanagerstring;
                            }
                            else
                            {
                                Toast.makeText(mContext.getApplicationContext(), "Unknown path", Toast.LENGTH_LONG).show();
                                Log.e("Bitmap", "Unknown path");
                            }

                            if (filePath != null)
                            {
                                String imgLink = filePath;
                                bitmap = decodeFile(imgLink);

                                mImageView.setImageBitmap(bitmap);
                                mImageView.setFilePath(filePath);
                            }
                            else
                            {
                                bitmap = null;
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(mContext.getApplicationContext(), "Internal error", Toast.LENGTH_LONG).show();
                            Log.e(e.getClass().getName(), e.getMessage(), e);
                        }
                    }
                    else
                    {
                        try
                        {
                            //Picking last saved image from camera

                            String[] projection = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA };
                            String sort = MediaStore.Images.ImageColumns._ID + " DESC";

                            Cursor cursor = mContext.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sort);

                            try
                            {
                                cursor.moveToFirst();

                                String largeImagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
                                selectedImageUri = Uri.fromFile(new File(largeImagePath));
                            }
                            finally
                            {
                                //cursor.close();
                            }

                            // OI FILE Manager
                            String filemanagerstring = selectedImageUri.getPath();

                            // MEDIA GALLERY
                            String selectedImagePath = getPath(selectedImageUri);

                            if (selectedImagePath != null)
                            {
                                filePath = selectedImagePath;
                            }
                            else if (filemanagerstring != null)
                            {
                                filePath = filemanagerstring;
                            }
                            else
                            {
                                Toast.makeText(mContext.getApplicationContext(), "Unknown path", Toast.LENGTH_LONG).show();
                                Log.e("Bitmap", "Unknown path");
                            }

                            if (filePath != null)
                            {
                                String imgLink = filePath;
                                bitmap = decodeFile(imgLink);

                                mImageView.setImageBitmap(bitmap);
                                mImageView.setFilePath(filePath);
                            }
                            else
                            {
                                bitmap = null;
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("Error", e.getMessage());
                        }
                    }
                }
            }
            break;
            default:
        }
    }

    /** Check if this device has a camera */
    public boolean checkCameraHardware() {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Toast.makeText(mContext.getApplicationContext(), "No camera on this device", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private Uri getOutputMediaFileUri() {
        // give the image a name so we can store it in the phone's default location
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
        return uri;
    }

    public static Bitmap decodeFile(String filePath)
    {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true)
        {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
    }

    @SuppressLint("NewApi")
    public String getPath(Uri uri)
    {
        try
        {
            if(uri.toString().contains("document"))
            {
                String wholeID = DocumentsContract.getDocumentId(uri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = { MediaStore.Images.Media.DATA };

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor1 = mContext.getContentResolver().query(getUri(), column, sel, new String[]{ id }, null);

                String filePath = "";

                int columnIndex = cursor1.getColumnIndex(column[0]);

                if (cursor1.moveToFirst())
                {
                    filePath = cursor1.getString(columnIndex);
                }
                return filePath;
            }
            else
            {
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = mContext.managedQuery(uri, projection, null, null, null);
                if (cursor != null)
                {
                    // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                    // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();

                    return cursor.getString(column_index);
                }
                else
                    return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }

    }

    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri()
    {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public static float rotationForImage(Context context, Uri uri)
    {
        if (uri.getScheme().equals("content"))
        {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(
                    uri, projection, null, null, null);
            if (c.moveToFirst())
            {
                return c.getInt(0);
            }
        }
        else if (uri.getScheme().equals("file"))
        {
            try
            {
                ExifInterface exif = new ExifInterface(uri.getPath());
                int rotation = (int)exifOrientationToDegrees(
                        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            }
            catch (IOException e)
            {

            }
        }
        return 0f;
    }

    private static float exifOrientationToDegrees(int exifOrientation)
    {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    private void ResizeAndRotate(Uri selectedImageUri)
    {
        // getting real image width, height
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        float rotation =  rotationForImage(mContext, selectedImageUri);
        if (rotation != 0f)
        {
            matrix.preRotate(rotation);
        }
        // recreate the new Bitmap
        bitmap =  Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private Bitmap ResizeImage(Bitmap bitmap)
    {
        // getting real image width, height
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // new image width, height
        int newWidth = 500;
        int newHeight = 500;

        // resize

        // calculate exact new width, height of rotated image
        int nrh, nrw;
        if(width>height)
        {
            nrw = newWidth;
            nrh = newWidth*height/width;
        }
        else
        {
            nrh = newHeight;
            nrw = newHeight*width/height;
        }

        Matrix matrix = new Matrix();
        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) nrw) / width;
        float scaleHeight = ((float) nrh) / height;
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        bitmap =  Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return bitmap;
    }

}