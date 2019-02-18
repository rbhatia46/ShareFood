package com.food.nofoodwaste.actvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.food.nofoodwaste.utils.AppSharedPreferences;
import com.food.nofoodwaste.utils.MyConstants;
import com.food.nofoodwaste.R;
import com.food.nofoodwaste.utils.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText edtName,edtPhone;
    private boolean isVolunteer;
    private AppCompatCheckBox chkIsVolunteer;
    private String name,mobile;
    AppSharedPreferences appSharedPreferences;
    private HashMap<String ,String> postParams;
    private String deviceId;
    String requestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        appSharedPreferences = new AppSharedPreferences(getApplicationContext());
        appSharedPreferences.saveStringPreferences(MyConstants.PREF_KEY_DEVICE_ID,deviceId);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        chkIsVolunteer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isVolunteer = true;
                }else{
                    isVolunteer = true;
                }
            }
        });
    }

    private void doLogin() {
        name = edtName.getText().toString().trim();
        mobile = edtPhone.getText().toString().trim();
        if (isValidationSuccess()){
            doLoginTask();
            savePreferences();
        }else {
            //displayToast();
        }
    }

    private void savePreferences() {
        appSharedPreferences.saveStringPreferences(MyConstants.PREF_KEY_NAME,name);
        appSharedPreferences.saveStringPreferences(MyConstants.PREF_KEY_MOBILE,mobile);
        appSharedPreferences.saveBooleanPreferences(MyConstants.PREF_KEY_IS_VOLUNTEER, isVolunteer);
        appSharedPreferences.saveBooleanPreferences(MyConstants.PREF_KEY_IS_LOGGEDIN, true);
    }

    private void doLoginTask() {
        JSONObject object = new JSONObject();
        try {
            object.put("username", name);
            object.put("mobileNumber", mobile);
            object.put("isVolunteer", String.valueOf(isVolunteer));
            object.put("deviceId", String.valueOf(deviceId));
            object.put("deviceToken", "TestDeviceToken");
            requestParams = object.toString();
            Log.e("login","--->>> "+requestParams);
            new doLoginAsyncTask().execute();
        } catch (Exception ex) {
            displayToast(getString(R.string.unable_to_connect));
        }
    }

    private void displayToast(String toastMsg) {
        Toast.makeText(getApplicationContext(),toastMsg,Toast.LENGTH_SHORT).show();
    }

    private boolean isValidationSuccess() {
        boolean isSuccess = true;

        if (name.length() < 3){
            displayToast(getString(R.string.valid_name));
            isSuccess = false;
        }else if(!mobile.matches(MyConstants.REG_EXP_MOBILE)){
            displayToast(getString(R.string.valid_mobile));
            isSuccess = false;
        }
        return isSuccess;
    }

    private void initView() {
        edtName = (EditText)findViewById(R.id.edt_name);
        edtPhone = (EditText)findViewById(R.id.edt_phone);

        chkIsVolunteer = (AppCompatCheckBox)findViewById(R.id.chk_is_volunteer);
    }

    /*@Override
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    /**
     * Async task class to get json by making HTTP call
     * */
    private class doLoginAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler serviceHandler = new ServiceHandler();


            // Making a request to url and getting response
            //String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            String sUrl = MyConstants.URL_ROOT+"user/create";

            String jsonStr = serviceHandler.performPostCall(sUrl, requestParams);

            Log.e("Response: ", "--->>> " + jsonStr);

            if (jsonStr != null) try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    if (!jsonObj.isNull("id")){
                        if (!jsonObj.getString("id").equals("")){
                            appSharedPreferences.saveStringPreferences(MyConstants.PREF_KEY_ID, jsonObj.getString("id"));
                            appSharedPreferences.saveBooleanPreferences(MyConstants.PREF_KEY_IS_LOGGEDIN, true);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (!appSharedPreferences.getStringPreferences(MyConstants.PREF_KEY_ID).equals("")) {
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }else{
                displayToast(getString(R.string.unable_to_connect));
            }
        }

    }

}
