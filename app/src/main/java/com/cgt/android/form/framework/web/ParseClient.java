package com.cgt.android.form.framework.web;

import android.content.Context;
import android.widget.Toast;

import com.cgt.android.form.framework.configurations.ResponseCodes;
import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.models.Model;
import com.cgt.android.form.framework.utils.MediaUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    public void postResponse(String tableName, String jsonString, HashMap<String, File> fileMap) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            final ParseObject formDataObject = new ParseObject(tableName);
            formDataObject.put("jsonData", jsonObject);

            for (String key : fileMap.keySet()) {
                if (fileMap.get(key) != null) {
                    byte[] data = MediaUtil.convertFileToByteArray(fileMap.get(key));

                    if (data != null) {
                        ParseFile file = new ParseFile("myImage.jpg", data);
                        //file.saveInBackground();
                        file.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Saved successfully.
                                    Toast.makeText(mContext.getApplicationContext(), "Image Saved.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // The save failed.
                                    Toast.makeText(mContext.getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new ProgressCallback() {
                            public void done(Integer percentDone) {
                                // Update your progress spinner here. percentDone will be between 0 and 100.
                                Toast.makeText(mContext.getApplicationContext(), "progress : " + percentDone, Toast.LENGTH_SHORT).show();
                            }
                        });
                        formDataObject.put(key, file);
                    }
                }
            }

            //formDataObject.pinInBackground(); // Local Datastore
            //formDataObject.saveEventually(); // Saving Objects Offline
            formDataObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        Toast.makeText(mContext.getApplicationContext(), "Saved. Object Id: " + formDataObject.getObjectId(), Toast.LENGTH_SHORT).show();

                        responseModel = new Model();
                        responseModel.responseCode = ResponseCodes.RESPONSE_SUCCESS;
                        responseModel.responseMessage = "" + formDataObject.getObjectId();
                        serverResponseListener.onServerSuccess(responseModel);
                    } else {
                        // The save failed.
                        Toast.makeText(mContext.getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();

                        responseModel = new Model();
                        responseModel.responseCode = ResponseCodes.RESPONSE_FAILURE;
                        responseModel.responseMessage = "Failed to Save";
                        serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postResponse(String tableName, HashMap<String, String> columnValueMap, HashMap<String, File> fileMap) {
        try {

            final ParseObject formDataObject = new ParseObject(tableName);

            for (String key : columnValueMap.keySet()) {
                formDataObject.put(key, columnValueMap.get(key));
            }

            for (String key : fileMap.keySet()) {
                if (fileMap.get(key) != null) {
                    byte[] data = MediaUtil.convertFileToByteArray(fileMap.get(key));

                    if (data != null) {
                        ParseFile file = new ParseFile("myImage.jpg", data);
                        //file.saveInBackground();
                        file.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Saved successfully.
                                    Toast.makeText(mContext.getApplicationContext(), "Image Saved.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // The save failed.
                                    Toast.makeText(mContext.getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new ProgressCallback() {
                            public void done(Integer percentDone) {
                                // Update your progress spinner here. percentDone will be between 0 and 100.
                                Toast.makeText(mContext.getApplicationContext(), "progress : " + percentDone, Toast.LENGTH_SHORT).show();
                            }
                        });
                        formDataObject.put(key, file);
                    }
                }
            }

            //formDataObject.pinInBackground(); // Local Datastore
            //formDataObject.saveEventually(); // Saving Objects Offline

            formDataObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        Toast.makeText(mContext.getApplicationContext(), "Saved. Object Id: " + formDataObject.getObjectId(), Toast.LENGTH_SHORT).show();

                        responseModel = new Model();
                        responseModel.responseCode = ResponseCodes.RESPONSE_SUCCESS;
                        responseModel.responseMessage = "" + formDataObject.getObjectId();
                        serverResponseListener.onServerSuccess(responseModel);
                    } else {
                        // The save failed.
                        Toast.makeText(mContext.getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();

                        responseModel = new Model();
                        responseModel.responseCode = ResponseCodes.RESPONSE_FAILURE;
                        responseModel.responseMessage = "Failed to Save";
                        serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponse(String tableName) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereEqualTo("objectId", "4jdOTorIDL");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    for (int i = 0; i < scoreList.size(); i++) {
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

    public void getResponse(String tableName, String objectId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your result
                    responseModel = new Model();
                    responseModel.responseCode = ResponseCodes.RESPONSE_SUCCESS;
                    responseModel.responseMessage = "" + object.getString("jsonData");
                    serverResponseListener.onServerSuccess(responseModel);
                } else {
                    // something went wrong
                    responseModel = new Model();
                    responseModel.responseCode = ResponseCodes.RESPONSE_FAILURE;
                    responseModel.responseMessage = "Failed to Save";
                    serverResponseListener.onServerFailure(responseModel, "Something went wrong!!");
                }
            }
        });
    }
}