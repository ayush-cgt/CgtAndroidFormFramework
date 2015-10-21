package com.cgt.android.form.framework.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cgt.android.form.framework.R;
import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.models.Model;
import com.cgt.android.form.framework.utils.CommonUtil;
import com.cgt.android.form.framework.web.WebConstant;

public class MainActivity extends AppCompatActivity implements IOnServerResponse {

    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You sure to continue?", Snackbar.LENGTH_LONG)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                submitForm();
                            }
                        }).show();
            }
        });
    }

    private void submitForm() {

        CommonUtil commonUtil = new CommonUtil(this);
        commonUtil.submitFormData(null, this, WebConstant.WEB_SERVICE_PRE_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServerSuccess(Model responseData) {

    }

    @Override
    public void onServerFailure(Model responseData, String failureText) {

        if (!TextUtils.isEmpty(failureText)) {
            Toast.makeText(getApplicationContext(), failureText, Toast.LENGTH_LONG).show();
        }
    }
}
