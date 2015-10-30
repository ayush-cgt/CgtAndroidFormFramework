package com.cgt.android.form.framework.web;

import android.content.Context;
import android.widget.Toast;

import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.models.Model;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kst-android on 27/10/15.
 */
public class ParseClient {

    Context mContext;
    IOnServerResponse serverResponseListener;
    private Model responseModel;

    public ParseClient(Context mContext, IOnServerResponse serverResponseListener) {
        this.mContext = mContext;
        this.serverResponseListener = serverResponseListener;
    }

    public void postResponse(String tableName, String columnName, String columnValue) {
        try {
            JSONObject jsonObject = new JSONObject(columnValue);

            final ParseObject formDataObject = new ParseObject(tableName);
            formDataObject.put(columnName, jsonObject);
            //formDataObject.pinInBackground(); // Local Datastore
            //formDataObject.saveEventually(); // Saving Objects Offline
            formDataObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        Toast.makeText(mContext.getApplicationContext(), "Saved. Object Id: " + formDataObject.getObjectId(), Toast.LENGTH_SHORT).show();
                        serverResponseListener.onServerSuccess(responseModel);
                    } else {
                        // The save failed.
                        Toast.makeText(mContext.getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                        serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postResponse(String tableName, HashMap <String, String> columnValueMap) {
        try {

            final ParseObject formDataObject = new ParseObject(tableName);

            for ( String key : columnValueMap.keySet() ) {
                formDataObject.put(key, columnValueMap.get(key));
            }

            //formDataObject.pinInBackground(); // Local Datastore
            //formDataObject.saveEventually(); // Saving Objects Offline

            formDataObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        Toast.makeText(mContext.getApplicationContext(), "Saved. Object Id: " + formDataObject.getObjectId(), Toast.LENGTH_SHORT).show();
                        serverResponseListener.onServerSuccess(responseModel);
                    } else {
                        // The save failed.
                        Toast.makeText(mContext.getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                        serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponse(String tableName) {

        /*ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });*/

        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereEqualTo("objectId", "4jdOTorIDL");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    for(int i=0; i<scoreList.size(); i++) {
                        ParseObject parseObject = scoreList.get(i);
                        //parseObject.get
                    }

                    Toast.makeText(mContext.getApplicationContext(), "Saved. Object Id: " + scoreList.size(), Toast.LENGTH_SHORT).show();
                    serverResponseListener.onServerSuccess(responseModel);
                } else {
                    Toast.makeText(mContext.getApplicationContext(), "Failed to Get", Toast.LENGTH_SHORT).show();
                    serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
                }
            }
        });
    }

}
