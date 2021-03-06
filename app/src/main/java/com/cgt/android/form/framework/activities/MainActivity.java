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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cgt.android.form.framework.R;
import com.cgt.android.form.framework.interfaces.IOnServerResponse;
import com.cgt.android.form.framework.models.Model;
import com.cgt.android.form.framework.ui.CgtImageView;
import com.cgt.android.form.framework.utils.CommonUtil;

public class MainActivity extends AppCompatActivity implements IOnServerResponse {

    String TAG = "MainActivity";
    TextView seekBarValueTextView;
    CgtImageView profileImageView;

    CommonUtil commonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        commonUtil = new CommonUtil(this);

        initViews();

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

        //commonUtil.submitFormData(null, this, WebConstant.WEB_SERVICE_PRE_URL);

        if (commonUtil.isValid(null)) {
            //commonUtil.postParseApiResponse(this, "FormData");
            commonUtil.postParseApiResponse(this, "FormJsonData");
            //commonUtil.postResponse(this, WebConstant.WEB_SERVICE_PRE_URL);
            //commonUtil.postResponse(this, WebConstant.WEB_SERVICE_PRE_URL, profileImageView.getFilePath());
            //commonUtil.getParseApiResponse(this, "FormData", "");
            //commonUtil.getResponse(this, WebConstant.WEB_SERVICE_PRE_URL);
        }
    }

    void initViews() {
        seekBarValueTextView = (TextView) findViewById(R.id.seekBarValueTextView);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBarValueTextView.setText(seekBar.getProgress() + "/" + seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                seekBarValueTextView.setText(progress + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarValueTextView.setText(progress + "/" + seekBar.getMax());
            }
        });

        profileImageView = (CgtImageView) findViewById(R.id.profileImageView);
        /*commonUtil.displayImage("http://hdwgo.com/wp-content/uploads/2015/03/hd-buildings-picture.jpg", profileImageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, 400, 600, 0);*/

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //commonUtil.captureCameraImage(profileImageView);
                commonUtil.captureGalleryImage(profileImageView);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commonUtil.onActivityResult(requestCode, resultCode, data);
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
        Toast.makeText(getApplicationContext(), "" + responseData.responseCode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServerFailure(Model responseData, String failureText) {

        if (!TextUtils.isEmpty(failureText)) {
            //Toast.makeText(getApplicationContext(), failureText, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "" + responseData.responseCode, Toast.LENGTH_LONG).show();
        }
    }
}
