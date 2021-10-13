package com.call.newadmobsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AdImplement ad_implement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ad_implement = new AdImplement(this);

        AdImplement.BanneradLoad(this, ((RelativeLayout) findViewById(R.id.admob_bannerads)));

        AdImplement.Interstitialadload(this);
        AdImplement.loadRewardedAd(this);
        AdImplement.UnityInterestialLoad(this);
      //  AdImplement.UnityRewardedVideoAdLoad(this);
        AdImplement.Nativead(this, ((FrameLayout) findViewById(R.id.fl_adplaceholder)));

    }

    public void ad(View view) {
        AdImplement.InterLoad(this, new AdImplement.OnAdListner() {
            @Override
            public void OnClose() {


            }
        });
    }


    public void rewreded(View view) {
        AdImplement.ShowRewardedVideo(this, new AdImplement.Onrewared() {
            @Override
            public void userearnsucess() {

                Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void userearnunsucess() {

            }
        });

    }






    @Override
    protected void onDestroy() {
        AdImplement.OndestroyBannerAd();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        AdImplement.OnResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        AdImplement.OnPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
      //  finish();
        // super.onBackPressed();
    }

}