package com.muhammadzain.statuspro.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.muhammadzain.statuspro.R;
import com.muhammadzain.statuspro.databinding.ActivityWhatsAppBusinessBinding;

public class WhatsAppBusiness extends AppCompatActivity {
    private ActivityWhatsAppBusinessBinding mBinding;
    private NavController navController;
    private AdView mAdView;

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityWhatsAppBusinessBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        MobileAds.initialize(this,"ca-app-pub-4675301390533499~3490802655");

        mAdView = findViewById(R.id.adViewbusiness);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        navController = Navigation.findNavController(WhatsAppBusiness.this, R.id.fragment_whatsapp_business_nav);
        NavigationUI.setupWithNavController(mBinding.whatsappBusinessBottom,navController);
        setclick();
        setIntents();

    }

    private void setIntents() {
        if (getIntent()!=null){
            boolean isDeleteBSVideo=getIntent().getBooleanExtra("isDeleteBSVideo",false);
            if (isDeleteBSVideo){
                navController.navigate(R.id.videoBusinessFragment);
            }
        }
    }

    private void setclick() {
        mBinding.icBackBusineses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        if (isPackageAvilable("com.whatsapp.w4b")){


        }else {

            finish();
            Toast.makeText(WhatsAppBusiness.this, "App is not installed in your phone", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.whatsapp.w4b"));
            startActivity(intent);
        }
        super.onStart();
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
}