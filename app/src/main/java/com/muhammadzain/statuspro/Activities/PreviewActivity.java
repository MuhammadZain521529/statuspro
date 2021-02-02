package com.muhammadzain.statuspro.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;
import com.muhammadzain.statuspro.Models.VideoModel;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.muhammadzain.statuspro.Adapter.ImageAdapter;
import com.muhammadzain.statuspro.Constant.Constants;
import com.muhammadzain.statuspro.Models.ModelImage;
import com.muhammadzain.statuspro.R;
import com.muhammadzain.statuspro.databinding.ActivityPreviewBinding;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {
    private boolean isVisible=false;
    private ActivityPreviewBinding mBinding;
    private ModelImage list;
    private List<ModelImage> listnextimage;
    private SubsamplingScaleImageView imageView;
    private boolean isVisibleShow=false;
    private NavController navController;
    private InterstitialAd mInterstitialAd;
    private ImageAdapter adapter;
    private static final String TAG="Swipe Position";
    private float x1,x2,y1,y2;
    private static int MIN_DISTANCE=150;
    private GestureDetector gestureDetector;
    private int position;
    private ModelImage nextimage;

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
        mBinding=ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

//                my orignal admod intretitial ad
        mInterstitialAd.setAdUnitId("ca-app-pub-4675301390533499/3833389910");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        setclick();
        Intents();


    }




    private void playVideo(ModelImage pathiv, int videoPosition) {

        pathiv= (ModelImage) getIntent().getSerializableExtra("iv");
        if(pathiv != null){
            String path = pathiv.getImagePath();

            if(path != null && !TextUtils.isEmpty(path)){
                position = videoPosition;

                Glide
                        .with(PreviewActivity.this)
                        .asBitmap()
                        .load(path)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView.setImage(ImageSource.bitmap(resource));
                            }
                        });

            }

        }


    }

    private void Intents() {
        Intent intent=getIntent();

        list= (ModelImage) getIntent().getSerializableExtra("iv");
        String image=getIntent().getStringExtra("image");



        if (list!=null){
            imageView=findViewById(R.id.preview_image);


            Glide
                    .with(PreviewActivity.this)
                    .asBitmap()
                    .load(list.getImagePath())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImage(ImageSource.bitmap(resource));
                        }
                    });
        }

    }

    private void setclick() {

        mBinding.icAddBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible=!isVisible;
                tooglefabs(isVisible);
            }
        });
        mBinding.icDownloadBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible=!isVisible;
                tooglefabs(isVisible);

                try {
                    downloadImage(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mBinding.icDeleteBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible=!isVisible;
                tooglefabs(isVisible);
                String path = list.getImagePath();
                final File file = new File(String.valueOf(path));

                new FancyAlertDialog.Builder(PreviewActivity.this)
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
                                String path = list.getImagePath();
                                final File file = new File(String.valueOf(path));
                                try {
                                    if (file.exists()) {
                                        file.delete();
                                        Toast.makeText(PreviewActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(PreviewActivity.this, Whatsapp.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
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
                Toast.makeText(PreviewActivity.this, "Please Wait....", Toast.LENGTH_LONG).show();
                isVisible=!isVisible;
                tooglefabs(isVisible);

                new Thread(new Runnable() {
                    @Override
                    public void run() {




                        File file=new File(String.valueOf(list.getImagePath()));
                        Uri uri_new= Uri.fromFile(file);

                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        if (uri_new.toString().endsWith(".jpg")){


                            Uri uri= FileProvider.getUriForFile(PreviewActivity.this,getApplicationContext().getPackageName()+".provider",fileur);
                            if (uri !=null){


                                Intent intent=new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                intent.setDataAndType(uri,"image/*");
                                intent.putExtra(Intent.EXTRA_STREAM,uri);

                                startActivity(Intent.createChooser(intent,"Share via"));

                            }else {
                                Toast.makeText(PreviewActivity.this, "Unable to display image.....", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }).start();



            }
        });
        mBinding.icRepostBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewActivity.this, "Please Wait....", Toast.LENGTH_SHORT).show();
                isVisible=!isVisible;
                tooglefabs(isVisible);


                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        File file=new File(String.valueOf(list.getImagePath()));
                        Uri uri_new= Uri.fromFile(file);
                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        Uri uri= FileProvider.getUriForFile(PreviewActivity.this,getApplicationContext().getPackageName()+".provider",fileur);

                        if(isValid(file)){
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("image/*");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM,uri);
                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            sharingIntent.setPackage("com.whatsapp");
                            try {
                                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(PreviewActivity.this, "No application found to open this file.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).start();

            }
        });
        mBinding.icSetAsBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewActivity.this, "Please Wait....", Toast.LENGTH_SHORT).show();
                isVisible=!isVisible;
                tooglefabs(isVisible);

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        File file=new File(String.valueOf(list.getImagePath()));
                        Uri uri_new= Uri.fromFile(file);
                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        Uri uri= FileProvider.getUriForFile(PreviewActivity.this,getApplicationContext().getPackageName()+".provider",fileur);

                        if(isValid(file)){
                            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(uri, "image/jpeg");
                            intent.putExtra("mimetype", "image/jpeg");

                            try {
                                startActivityForResult(Intent.createChooser(intent,"Set Image As"),200);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(PreviewActivity.this, "No application found to open this file.", Toast.LENGTH_LONG).show();
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

    private void downloadImage(ModelImage list) throws IOException {
        File files = new File(Constants.APP_DIR);
        if (!files.exists()){
            files.mkdirs();
        }
        File destfile=new File(files+ File.separator+list.getTitle());
        if (destfile.exists()){
            destfile.delete();
        }
        copyFile(list.getImagePath(),destfile);
        Snackbar.make(imageView,"Image Saved Successfully", Snackbar.LENGTH_LONG)
                .setAction("Open Saved Gallery", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent=new Intent(PreviewActivity.this,SavedGallery.class);
                       intent.putExtra("dnimage",list.getImagePath());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                       finish();
                    }
                }).show();


        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setDataAndType(Uri.fromFile(destfile),"image/*");
        getApplicationContext().sendBroadcast(intent);



    }




    private void copyFile(String file, File destfile) throws IOException {

        if (!destfile.getParentFile().exists()){
            destfile.getParentFile().mkdirs();
        }
        if (!destfile.exists()){
            destfile.createNewFile();
        }
        FileChannel source=null;
        FileChannel destination=null;
        source=new FileInputStream(String.valueOf(file)).getChannel();
        destination=new FileOutputStream(destfile).getChannel();
        destination.transferFrom(source,0,source.size());
        source.close();
        destination.close();

    }

    private void tooglefabs(boolean showit) {

        int icon=R.drawable.ic_add;
        if (showit){

            mBinding.icDownloadBtnFb.setVisibility(View.VISIBLE);
            mBinding.icShareBtnFb.setVisibility(View.VISIBLE);
            mBinding.icDeleteBtnFb.setVisibility(View.VISIBLE);
            mBinding.icSetAsBtnFb.setVisibility(View.VISIBLE);
            mBinding.icRepostBtnFb.setVisibility(View.VISIBLE);
            mBinding.saveTvImage.setVisibility(View.VISIBLE);
            mBinding.shareTvImage.setVisibility(View.VISIBLE);
            mBinding.repostTvImage.setVisibility(View.VISIBLE);
            mBinding.delteTvImage.setVisibility(View.VISIBLE);
            mBinding.setAsTvImage.setVisibility(View.VISIBLE);
            icon=R.drawable.ic_cross;
        }else {


            mBinding.icDownloadBtnFb.setVisibility(View.GONE);
            mBinding.icShareBtnFb.setVisibility(View.GONE);
            mBinding.icDeleteBtnFb.setVisibility(View.GONE);
            mBinding.icSetAsBtnFb.setVisibility(View.GONE);
            mBinding.icRepostBtnFb.setVisibility(View.GONE);
            mBinding.saveTvImage.setVisibility(View.GONE);
            mBinding.shareTvImage.setVisibility(View.GONE);
            mBinding.repostTvImage.setVisibility(View.GONE);
            mBinding.delteTvImage.setVisibility(View.GONE);
            mBinding.setAsTvImage.setVisibility(View.GONE);
            icon=R.drawable.ic_add;

        }
        mBinding.icAddBtnFb.setImageDrawable(getResources().getDrawable(icon));
    }


}