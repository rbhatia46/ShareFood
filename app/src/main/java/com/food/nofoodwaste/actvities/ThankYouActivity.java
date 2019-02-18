package com.food.nofoodwaste.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.food.nofoodwaste.R;
import com.food.nofoodwaste.utils.AppSharedPreferences;
import com.food.nofoodwaste.utils.FoodObject;
import com.food.nofoodwaste.utils.MyConstants;

public class ThankYouActivity extends AppCompatActivity {

    private String fromActivity = "";
    private TextView txtThanksMsg,txtUserName,txtDonorAddress,txtDeliveryAddress,txtDonorMsg,txtDelieveryMsg;
    AppSharedPreferences appSharedPreferences;
    FoodObject donorFoodObj,deliveryFoodObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

        appSharedPreferences = new AppSharedPreferences(getApplicationContext());

        String userName = appSharedPreferences.getStringPreferences(MyConstants.PREF_KEY_NAME);
        if (userName.equals("")){
            userName = "User";
        }

        txtUserName = (TextView)findViewById(R.id.txt_user_name);
        txtThanksMsg = (TextView)findViewById(R.id.txt_thanks_msg);
        txtDeliveryAddress = (TextView)findViewById(R.id.txt_delivery_address);
        txtDonorAddress = (TextView)findViewById(R.id.txt_donor_address);
        txtDonorMsg= (TextView)findViewById(R.id.txt_donation_title);
        txtDelieveryMsg = (TextView)findViewById(R.id.txt_delivery_title);

        readIntentData();

        if (fromActivity == null) fromActivity = "";

        if (fromActivity.equals(MyConstants.KEY_DONOR)){
            addTextData(userName,getString(R.string.thankyou_donation));
        }else if(fromActivity.equals(MyConstants.KEY_VOLUNTEER)){
            addTextData(userName,getString(R.string.thankyou_volunteer));
        }else if(fromActivity.equals(MyConstants.KEY_MAP_LOCATION)){
            addTextData(userName,getString(R.string.thankyou_map_location));
        }

        if (donorFoodObj != null && donorFoodObj.getAddress() != null){
            txtDonorMsg.setVisibility(View.VISIBLE);
            txtDonorAddress.setVisibility(View.VISIBLE);
            txtDonorAddress.setText(donorFoodObj.getAddress());
            addTextData(userName, getString(R.string.thankyou_volunteer));
        }

        if (deliveryFoodObject != null && deliveryFoodObject.getAddress() != null){
            txtDelieveryMsg.setVisibility(View.VISIBLE);
            txtDeliveryAddress.setVisibility(View.VISIBLE);
            txtDeliveryAddress.setText(deliveryFoodObject.getAddress());
            addTextData(userName, getString(R.string.thankyou_volunteer));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addTextData(String userName, String thanksMsg) {
        txtUserName.setText("Dear "+userName);
        txtThanksMsg.setText(thanksMsg);
    }

    private void readIntentData() {
        try {
            Intent intent = getIntent();
            fromActivity = intent.getStringExtra(MyConstants.FROM_ACTIVITY);
            donorFoodObj = (FoodObject)intent.getSerializableExtra("DonationObj");
            deliveryFoodObject = (FoodObject)intent.getSerializableExtra("DeliveryObj");
        }catch (Exception e){

        }
    }
}
