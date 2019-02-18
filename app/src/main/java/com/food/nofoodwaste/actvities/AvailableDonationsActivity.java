package com.food.nofoodwaste.actvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.food.nofoodwaste.R;
import com.food.nofoodwaste.adapters.DonationsListAdapter;
import com.food.nofoodwaste.utils.FoodObject;
import com.food.nofoodwaste.utils.MyConstants;
import com.food.nofoodwaste.utils.OnItemClickListener;
import com.food.nofoodwaste.utils.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AvailableDonationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonationsListAdapter donationsListAdapter;
    private ArrayList<FoodObject> foodObjects;
    private OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_donations);
        //initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getApplicationContext(),"clicked: "+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),AvailableDeliveryPlacesActivity.class);
                intent.putExtra("DonationObj",foodObjects.get(position));
                startActivity(intent);
            }
        };

        foodObjects = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager llm = new LinearLayoutManager(AvailableDonationsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        donationsListAdapter = new DonationsListAdapter(getApplicationContext(),foodObjects);
        donationsListAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(donationsListAdapter);

        loadLocations();

    }

    private void loadLocations() {
        new loadLocationsAsyncTask().execute();
    }

    private void displayToast(String toastMsg) {
        Toast.makeText(getApplicationContext(),toastMsg,Toast.LENGTH_SHORT).show();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class loadLocationsAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AvailableDonationsActivity.this);
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
            String sUrl = MyConstants.URL_ROOT+"donate/distance";

            String jsonStr = serviceHandler.performGetCall(sUrl);

            Log.e("Response: ", "--->>> " + jsonStr);

            if (jsonStr != null) try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                if (jsonArray != null && jsonArray.length() > 0) {
                    convertJsonAsObj(jsonArray);
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
            if (foodObjects.size()> 0){
                loadAdapter();
            }
        }

    }

    private void convertJsonAsObj(JSONArray jsonArray) {
        try{
            for (int i = 0;i < jsonArray.length() ;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FoodObject foodObject = new FoodObject();
                if (!jsonObject.isNull("donorMobile"))
                    foodObject.setMobile(jsonObject.getString("donorMobile"));
                //foodObject.setId(jsonObject.getString("id"));
                if (!jsonObject.isNull("foodType"))
                foodObject.setFoodtype(jsonObject.getString("foodType"));

                if (!jsonObject.isNull("quantity"))
                foodObject.setQuantity(jsonObject.getString("quantity"));

                if (!jsonObject.isNull("address"))
                foodObject.setAddress(jsonObject.getString("address"));

                if (!jsonObject.isNull("latitude"))
                foodObject.setLat(jsonObject.getString("latitude"));

                if (!jsonObject.isNull("longitude"))
                foodObject.setLng(jsonObject.getString("longitude"));

                if (!jsonObject.isNull("distance"))
                foodObject.setDistance(jsonObject.getString("distance"));
                //foodObject.sets(jsonObject.getString("donationStatus"));
                foodObjects.add(foodObject);
            }
        }catch (Exception e){}
    }

    private void loadAdapter() {
        if (foodObjects.size() > 0) {
            donationsListAdapter = new DonationsListAdapter(getApplicationContext(), foodObjects);
            donationsListAdapter.setOnItemClickListener(onItemClickListener);
            recyclerView.setAdapter(donationsListAdapter);
            //donationsListAdapter.setOnItemClickListener(onItemClickListener);
        }
    }

}
