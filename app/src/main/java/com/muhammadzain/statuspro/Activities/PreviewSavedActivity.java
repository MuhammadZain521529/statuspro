package com.muhammadzain.statuspro.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.muhammadzain.statuspro.Models.SavedModel;
import com.muhammadzain.statuspro.R;
import com.muhammadzain.statuspro.databinding.ActivityPreviewSavedBinding;
import java.io.File;
public class PreviewSavedActivity extends AppCompatActivity {
    private ActivityPreviewSavedBinding mBinding;
    private android.view.animation.Animation bottom_animation;
    private SubsamplingScaleImageView imageViewsavedactivity;
    private boolean isPreview=false;
    private SavedModel list;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onBackPressed() {
        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            if(mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }

        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding=ActivityPreviewSavedBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        //        my orignal admod intretitial ad
        mInterstitialAd.setAdUnitId("ca-app-pub-4675301390533499/3833389910");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        clicks();
        getIntents();

    }


    private void getIntents() {

        imageViewsavedactivity=findViewById(R.id.preview_saved_iv);
        Intent intent=getIntent();
        list= (SavedModel) intent.getSerializableExtra("savedimage");
        if (list!=null){
            Glide
                    .with(PreviewSavedActivity.this)
                    .asBitmap()
                    .load(list.getPath())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageViewsavedactivity.setImage(ImageSource.bitmap(resource));

                        }
                    });

        }
    }

    private void clicks() {
        mBinding.icAddBtnSavedFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isPreview=!isPreview;
                togglefabs(isPreview);


            }
        });
        mBinding.icDeleteBtnSavedFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPreview=!isPreview;
                togglefabs(isPreview);


                        new FancyAlertDialog.Builder(PreviewSavedActivity.this)
                                .setTitle("Are you sure you want to delete this file ?")
                                .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                                .setNegativeBtnText("Cancel")
                                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                                .setPositiveBtnText("Yes")
                                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                                .setAnimation(Animation.POP)
                                .isCancellable(true)
                                .setIcon(R.drawable.ic_dialog_delete, Icon.Visible)
                                .OnPositiveClicked(new FancyAlertDialogListener() {
                                    @Override
                                    public void OnClick() {
                                        String path = list.getPath();
                                        final File file = new File(path);
                                        try {
                                            if (file.exists()) {
                                                file.delete();
                                                Toast.makeText(PreviewSavedActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(PreviewSavedActivity.this,SavedGallery.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();

                                            }
                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .OnNegativeClicked(new FancyAlertDialogListener() {
                                    @Override
                                    public void OnClick() {


                                    }
                                })
                                .build();


            }
        });
        mBinding.icShareBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewSavedActivity.this, "Please Wait.....", Toast.LENGTH_SHORT).show();
                isPreview=!isPreview;
                togglefabs(isPreview);
                new Thread(new Runnable() {
                    @Override
                    public void run() {



                        File file=new File(list.getPath());
                        Uri uri_new= Uri.fromFile(file);

                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        if (uri_new.toString().endsWith(".jpg")){


                            Uri uri= FileProvider.getUriForFile(PreviewSavedActivity.this,getApplicationContext().getPackageName()+".provider",fileur);
                            if (uri !=null){

                                Intent intent=new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                intent.setDataAndType(uri,"image/*");
                                intent.putExtra(Intent.EXTRA_STREAM,uri);

                                startActivity(Intent.createChooser(intent,"Share via"));

                            }else {
                                Toast.makeText(PreviewSavedActivity.this, "Unable to display image.....", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }).start();


            }
        });
        mBinding.icRepostSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewSavedActivity.this, "Please Wait.....", Toast.LENGTH_SHORT).show();
                isPreview=!isPreview;
                togglefabs(isPreview);

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        File file=new File(list.getPath());
                        Uri uri_new= Uri.fromFile(file);
                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        Uri uri= FileProvider.getUriForFile(PreviewSavedActivity.this,getApplicationContext().getPackageName()+".provider",fileur);


                        if(isValid(file)){
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("image/*");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            sharingIntent.setPackage("com.whatsapp");
                            try {
                                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(PreviewSavedActivity.this, "No application found to open this file.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).start();


            }
        });
        mBinding.icSetAsSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewSavedActivity.this, "Please Wait.....", Toast.LENGTH_SHORT).show();
                isPreview=!isPreview;
                togglefabs(isPreview);
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        File file=new File(list.getPath());
                        Uri uri_new= Uri.fromFile(file);
                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        Uri uri= FileProvider.getUriForFile(PreviewSavedActivity.this,getApplicationContext().getPackageName()+".provider",fileur);


                        if(isValid(file)){
                            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(uri, "image/jpeg");
                            intent.putExtra("mimetype", "image/jpeg");

                            try {
                                startActivity(Intent.createChooser(intent, "Set as:"));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(PreviewSavedActivity.this, "No application found to open this file.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    private boolean isValid(File file) {


        String name=file.getName();
        String extension = name.substring(name.lastIndexOf('.'));

        if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".JPEG"))
            return true;

//

        return false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void togglefabs(boolean isShowing) {
        int icon= R.drawable.ic_add;
        if (isShowing){
            mBinding.icShareBtnFb.setVisibility(View.VISIBLE);
            mBinding.icDeleteBtnSavedFb.setVisibility(View.VISIBLE);
            mBinding.icRepostSv.setVisibility(View.VISIBLE);
            mBinding.icSetAsSv.setVisibility(View.VISIBLE);
            mBinding.shareTv.setVisibility(View.VISIBLE);
            mBinding.deleteTv.setVisibility(View.VISIBLE);
            mBinding.repostTv.setVisibility(View.VISIBLE);
            mBinding.setASTv.setVisibility(View.VISIBLE);



            icon=R.drawable.ic_cross;
        }else {
            mBinding.icShareBtnFb.setVisibility(View.GONE);
            mBinding.icDeleteBtnSavedFb.setVisibility(View.GONE);
            mBinding.icRepostSv.setVisibility(View.GONE);
            mBinding.icSetAsSv.setVisibility(View.GONE);
            mBinding.shareTv.setVisibility(View.GONE);
            mBinding.deleteTv.setVisibility(View.GONE);
            mBinding.repostTv.setVisibility(View.GONE);
            mBinding.setASTv.setVisibility(View.GONE);



            icon=R.drawable.ic_add;
        }
        mBinding.icAddBtnSavedFb.setImageDrawable(getResources().getDrawable(icon));
    }
}