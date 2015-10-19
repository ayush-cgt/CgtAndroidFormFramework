package com.cgt.android.form.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat")
public class Utilities 
{
	public static boolean checkNetworkConnection(Context paramContext)
	{
	    int i = 1;
	    boolean flag=true;
	    NetworkInfo localNetworkInfo1 = ((ConnectivityManager)paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(i);
	    NetworkInfo localNetworkInfo2 = ((ConnectivityManager)paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	    System.out.println("wifi" + localNetworkInfo1.isAvailable());
	    System.out.println("info" + localNetworkInfo2);
	    if (((localNetworkInfo2 == null) || (!localNetworkInfo2.isConnected())) && (!localNetworkInfo1.isAvailable()))
	      i = 0;
	    if(i == 0)
	    	flag = false;
	    if(i==1)
	    	flag = true;
		return flag;
	}

	public static boolean isNetworkOnline(Context paramContext)
	{
		boolean status=false;
		try
		{
			ConnectivityManager cm = (ConnectivityManager) paramContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED)
			{
				status= true;
			}
			else
			{
				netInfo = cm.getNetworkInfo(1);
				if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
					status= true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return status;
	}

	public static String getStringResourceByName(Context paramContext, String aString) 
	{		
		int resId = paramContext.getResources().getIdentifier(aString , "string", paramContext.getApplicationContext().getPackageName());
		Log.e("key", Integer.toString(resId));
		return paramContext.getString(resId);
	}
	
	// append two list view
	public static void setListViewHeightBasedOnChildren(ListView listView) 
	{
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) 
        {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) 
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    

    
    /************************** Math formulas **************************/
    public static double GetDistance(double lat1, double lon1, double lat2, double lon2)
	{
	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515; // in miles
	      dist = dist * 1.62137; // to convert miles in kms
	      return (dist);
	}		

    public static double GetBearingAngle(double lat1, double lon1, double lat2, double lon2)
    {  
        lat1 = deg2rad(lat1);
        lon1 = deg2rad(lon1);
        lat2 = deg2rad(lat2);
        lon2 = deg2rad(lon2);

        double deltaLong = lon2 - lon1;

        double y = Math.sin(deltaLong) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLong);
        double bearing = Math.atan2(y, x);
        return ConvertToBearing(rad2deg(bearing));
    }

    public static double ConvertToBearing(double deg)
    {
        return (deg + 360) % 360;
    }
    
    public static double deg2rad(double deg) 
	{
		  return (deg * Math.PI / 180.0);
	}
   
    public static double rad2deg(double rad) 
	{
	   	  return (rad * 180.0 / Math.PI);
	}
	/************************** Math formulas **************************/
    
    // make two digit numeric value
    public static String pad(int c) 
	{
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
    
    
    // Cut Image in circle with specific width height
    public static Bitmap getclip(Bitmap bitmap)
	{
		Bitmap sbmp = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
	    Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    
	    canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(sbmp, rect, rect, paint);
	    return output;	    
	}
    


}
