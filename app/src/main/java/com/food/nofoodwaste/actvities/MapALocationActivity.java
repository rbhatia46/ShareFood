package com.food.nofoodwaste.actvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.food.nofoodwaste.R;
import com.food.nofoodwaste.utils.AppSharedPreferences;
import com.food.nofoodwaste.utils.MyConstants;
import com.food.nofoodwaste.utils.ServiceHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MapALocationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private EditText edtAddress;
    private String address = "",lat = "",lng = "",userid= "";
    //private HashMap<String ,String> postParams;
    private String requestParams;
    AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_a_location);
        //initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appSharedPreferences = new AppSharedPreferences(getApplicationContext());
        userid = appSharedPreferences.getStringPreferences(MyConstants.PREF_KEY_ID);

        edtAddress = (EditText)findViewById(R.id.edt_address);

        mGoogleApiClient = new GoogleApiClient
                .Builder( this )
                .enableAutoManage( this, 0, this )
                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .build();

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.mipma);
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.txt_select_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPlacePicker();
            }
        });

        findViewById(R.id.btn_map_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDonation();
            }
        });
    }

    private void submitDonation() {
        address = edtAddress.getText().toString().trim();

        if (isValidationSuccess()){
            doSubmitDonationTask();
        }
    }

    private void doSubmitDonationTask() {
/*
        postParams = new HashMap<>();
        postParams.put("userid",userid);
        postParams.put("latitude",lat);
        postParams.put("longitude",lng);
        postParams.put("address", address);
*/

        JSONObject object = new JSONObject();
        try {
            object.put("consumerName", appSharedPreferences.getStringPreferences(MyConstants.PREF_KEY_NAME));
            object.put("consumerMobile", appSharedPreferences.getStringPreferences(MyConstants.PREF_KEY_MOBILE));
            //object.put("isVolunteer", String.valueOf(appSharedPreferences.getStringPreferences(MyConstants.PREF_KEY_IS_VOLUNTEER)));
            object.put("isActive", "true");
//            object.put("deviceId", String.valueOf(appSharedPreferences.getStringPreferences(MyConstants.PREF_KEY_DEVICE_ID)));
            object.put("quantity", "20");
            object.put("latitude", lat);
            object.put("longitude", lng);
            object.put("address", address);
//            object.put("deviceToken", "TestDeviceToken");
            requestParams = object.toString();
            Log.e("Map Request params","--->>> "+requestParams);
            new doSubmitDonationAsyncTask().execute();
        } catch (Exception ex) {
            displayToast(getString(R.string.unable_to_connect));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            //mAdapter.setGoogleApiClient( null );
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void displayPlacePicker() {
        if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
            return;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult( builder.build( getApplicationContext() ), PLACE_PICKER_REQUEST );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d("PlacesAPI Demo", "GooglePlayServicesRepairableException thrown");
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }
    }

    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if( requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK ) {
            displayPlace( PlacePicker.getPlace( data, this ) );
        }
    }

    private void displayPlace( Place place ) {
        if( place == null )
            return;

        //DisplayLog.displayNormalLog("MainActivity","Place",place);

        String content = "";
        //if( !TextUtils.isEmpty(place.getName()) ) {
        //    content += "Name: " + place.getName() + "\n";
        //}
        if( !TextUtils.isEmpty(place.getAddress()) ) {
            content += place.getAddress();
        }
        //if( !TextUtils.isEmpty( place.getPhoneNumber() ) ) {
        //    content += "Phone: " + place.getPhoneNumber();
        //}

        if( !TextUtils.isEmpty( String.valueOf(place.getLatLng()) ) ) {
            Log.e("MainActivity", "Latlong: "+String.valueOf(place.getLatLng()));
            LatLng mLatLng = place.getLatLng();
            //isplayLog.displayNormalLog("MainActivity", "Latlong Points", mLatLng.latitude+" - "+mLatLng.longitude);
            lat = String.valueOf(mLatLng.latitude);
            lng = String.valueOf(mLatLng.longitude);

        }
        Log.e("Main ac","address: "+content);

        if (content != null && !content.equals("")){
            edtAddress.setText(content);
        }else {
            edtAddress.setText("");
        }
        //mTextView.setText( content );
       // medtAddress.setText(content);
    }

    private boolean isValidationSuccess(){
        boolean isSuccess = true;
        if (address.equals("") || address.length() < 5){
            displayToast("Please enter the correct address");
            isSuccess = false;
        }else if (lat.equals("") || lng.equals("")){
            displayToast("Please select the location");
            isSuccess = false;
        }
        return isSuccess;
    }

    private void displayToast(String toastMsg) {
        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class doSubmitDonationAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapALocationActivity.this);
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
            String sUrl = MyConstants.URL_ROOT+"consumer/create";

            String jsonStr = serviceHandler.performPostCall(sUrl, requestParams);

            Log.e("Map Response: ", "--->>> " + jsonStr);

            if (jsonStr != null) try {
                JSONObject jsonObj = new JSONObject(jsonStr);
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
            Intent intent =new Intent(getApplicationContext(),ThankYouActivity.class);
            startActivity(intent);
            intent.putExtra(MyConstants.FROM_ACTIVITY,MyConstants.KEY_MAP_LOCATION);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        }

    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
