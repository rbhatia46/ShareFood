package com.food.nofoodwaste.actvities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.food.nofoodwaste.R;
import com.food.nofoodwaste.utils.AppSharedPreferences;
import com.food.nofoodwaste.utils.MyConstants;

public class Splashscreen extends Activity {

    public int Time_DelayForRequest = 1000;
    public int splashScreentime = 2 * Time_DelayForRequest;
    Thread background;
    AppSharedPreferences appSharedPreferences;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        appSharedPreferences = new AppSharedPreferences(Splashscreen.this);
        final boolean isLoggedIn = appSharedPreferences.getBooleanPreferences(MyConstants.PREF_KEY_IS_LOGGEDIN);

        background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(splashScreentime);

                    if (isLoggedIn) {
                        Intent myIntent = new Intent(Splashscreen.this, DashBoardActivity.class);
                        //myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(myIntent);

                    } else {
                        Intent myIntent = new Intent(Splashscreen.this, LoginActivity.class);
                        //myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(myIntent);

                    }


                    // After 5 seconds redirect to another intent
                    // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    //Remove activity_selection_list
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}
