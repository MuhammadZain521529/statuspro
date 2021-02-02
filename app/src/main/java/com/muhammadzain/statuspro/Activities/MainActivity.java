package com.muhammadzain.statuspro.Activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.muhammadzain.statuspro.BuildConfig;
import com.thesoftparrot.jkpm.JKPMActivity;
import com.muhammadzain.statuspro.R;
import com.muhammadzain.statuspro.databinding.ActivityMainBinding;

public class MainActivity extends JKPMActivity {
    private ActivityMainBinding mBinding;
    private static final String[] PERMISSION =new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SET_WALLPAPER
    };
    private AdView mAdView;


    @Override
    public void onBackPressed() {
        this.finish();

    }





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
//        MobileAds.initialize(this,"ca-app-pub-4675301390533499~3490802655");
//        setSupportActionBar(mBinding.toolbarMain);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        if (!areAllPermissionsEnabled(PERMISSION))
            askRuntimePermissions(PERMISSION);

        setClicks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id == R.id.share_app){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }
        if (id == R.id.rate_click){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setClicks() {
        mBinding.whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            Intent intent=new Intent(MainActivity.this,Whatsapp.class);
                            startActivity(intent);
                    }
                }).start();

            }
        });
        mBinding.whatsappBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        Intent intent=new Intent(MainActivity.this,WhatsAppBusiness.class);
                        startActivity(intent);
                    }
                }).start();
            }
        });
        mBinding.savedGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent=new Intent(MainActivity.this,SavedGallery.class);
                        startActivity(intent);

                    }
                }).start();

            }
        });


    }

    @Override
    protected void onPermissionsGranted() {

    }

    @Override
    protected void onPermissionsDenied() {

        showDenialBox();
    }
}