package com.muhammadzain.statuspro.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.muhammadzain.statuspro.R;
import com.muhammadzain.statuspro.databinding.ActivitySavedGalleryBinding;

public class SavedGallery extends AppCompatActivity {

    private ActivitySavedGalleryBinding mBinding;
    private NavController navController;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivitySavedGalleryBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        MobileAds.initialize(this,"ca-app-pub-4675301390533499~3490802655");
        mAdView = findViewById(R.id.adViewsaved);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        navController= Navigation.findNavController(SavedGallery.this, R.id.fragment_saved_nav);
        NavigationUI.setupWithNavController(mBinding.savedGalleryBottom,navController);
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        getintents();
        setclicks();

    }

    private void getintents() {

        if (getIntent()!=null){
            boolean isWhatsappVideo=getIntent().getBooleanExtra("isWhatsappVideo",false);
            boolean isWhatsappBusinessVideo=getIntent().getBooleanExtra("isWhatsappBusinessVideo",false);
            boolean isDeleteVideo=getIntent().getBooleanExtra("isDeleteVideo",false);
            if (isWhatsappVideo){
                navController.navigate(R.id.savedVideoFragment);
            }
            if (isWhatsappBusinessVideo){
                navController.navigate(R.id.savedVideoFragment);
            }
            if (isDeleteVideo){
                navController.navigate(R.id.savedVideoFragment);
            }
        }
    }





    @Override
    public void onBackPressed() {
              finish();
    }

    private void setclicks() {
        mBinding.icBackSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           finish();
            }
        });

    }
}