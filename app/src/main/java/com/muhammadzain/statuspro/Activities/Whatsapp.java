package com.muhammadzain.statuspro.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.muhammadzain.statuspro.Adapter.ImageAdapter;
import com.muhammadzain.statuspro.R;
import com.muhammadzain.statuspro.databinding.ActivityWhatsappBinding;

public class Whatsapp extends AppCompatActivity {
    private ActivityWhatsappBinding mBinding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavController navController;

    private ImageAdapter adapterimage;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityWhatsappBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        MobileAds.initialize(this,"ca-app-pub-4675301390533499~3490802655");

        mAdView = findViewById(R.id.adViewwhatsapp);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        navController= Navigation.findNavController(Whatsapp.this, R.id.fragment_whatsapp_nav);
        NavigationUI.setupWithNavController(mBinding.whatsappBottom,navController);
        setupToolbar();
        setIntents();

    }


    private void setIntents() {
        if (getIntent()!=null){
            boolean isDeleteWpVideo=getIntent().getBooleanExtra("isDeleteWpVideo",false);
            if (isDeleteWpVideo){
                navController.navigate(R.id.videoFragment);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isPackageAvilable("com.whatsapp")){


        }else {

            finish();
            Toast.makeText(Whatsapp.this, "App is not installed in your phone", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.whatsapp"));
            startActivity(intent);
        }
    }
    private boolean isPackageAvilable(String name){
        boolean available=true;
        try {
            getPackageManager().getPackageInfo(name,0);
        } catch (PackageManager.NameNotFoundException e) {
            available=false;
        }
        return available;
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void setupToolbar() {

        actionBarDrawerToggle = new ActionBarDrawerToggle(Whatsapp.this, mBinding.drawerLayout, mBinding.toolbarWhatsapp, R.string.app_name, R.string.app_name);
        mBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent=new Intent(Whatsapp.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.status_gallery:
                        Intent intentgallery=new Intent(Whatsapp.this,SavedGallery.class);
                        startActivity(intentgallery);

                        break;
                    case R.id.privacy_policy:
                        String url = "https://statussaverpro786.blogspot.com/p/privacy-policy.html";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case R.id.rating:

                        break;
                }
                return false;
            }
        });
    }
}