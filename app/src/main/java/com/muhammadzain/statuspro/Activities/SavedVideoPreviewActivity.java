package com.muhammadzain.statuspro.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.muhammadzain.statuspro.Models.SavedModel;
import com.muhammadzain.statuspro.R;
import com.muhammadzain.statuspro.databinding.ActivitySavedVideoPreviewBinding;
import java.io.File;
import java.util.List;
public class SavedVideoPreviewActivity extends AppCompatActivity {
    private List<SavedModel> list;
    private PlayerView mVideoView;
    private ImageView full_sc_btn,mPlaynext,mPlayPrev;
    private SimpleExoPlayer mExoPlayer;
    private boolean isVisible=false;
    private boolean flag=false;
    private InterstitialAd mInterstitialAd;

    private int position;
    private ActivitySavedVideoPreviewBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding=ActivitySavedVideoPreviewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");


        //        my orignal admod intretitial ad
        mInterstitialAd.setAdUnitId("ca-app-pub-4675301390533499/3833389910");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        initRef();
        handleclicks();

    }


    private void handleclicks() {
        mBinding.icAddFabSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible=!isVisible;
                toogleFabs(isVisible);
            }
        });
        mBinding.icDeleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible=!isVisible;
                toogleFabs(isVisible);
                String path = list.get(position).getPath();
                final File file = new File(path);

                new FancyAlertDialog.Builder(SavedVideoPreviewActivity.this)
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
                                String path = list.get(position).getPath();
                                final File file = new File(path);
                                try {
                                    if (file.exists()) {
                                        file.delete();
                                        Toast.makeText(SavedVideoPreviewActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(SavedVideoPreviewActivity.this,SavedGallery.class);
                                        intent.putExtra("isDeleteVideo",true);
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
        mBinding.icRepostVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SavedVideoPreviewActivity.this, "Please Wait....", Toast.LENGTH_SHORT).show();
                isVisible=!isVisible;
                toogleFabs(isVisible);
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        File file=new File(list.get(position).getPath());
                        Uri uri_new= Uri.fromFile(file);
                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        Uri uri= FileProvider.getUriForFile(SavedVideoPreviewActivity.this,getApplicationContext().getPackageName()+".provider",fileur);


                        if(isValid(file)){
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("video/*");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            sharingIntent.setPackage("com.whatsapp");
                            try {
                                startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(SavedVideoPreviewActivity.this, "No application found to open this file.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).start();





            }
        });
        mBinding.icShareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SavedVideoPreviewActivity.this, "Please Wait....", Toast.LENGTH_SHORT).show();
                isVisible=!isVisible;
                toogleFabs(isVisible);
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        String path = list.get(position).getPath();
                        File file=new File(path);
                        Uri uri_new= Uri.fromFile(file);

                        Uri uri12 = Uri.parse(uri_new.toString());
                        File fileur = new File(uri12.getPath());
                        if (uri_new.toString().endsWith(".mp4")){


                            Uri uri= FileProvider.getUriForFile(SavedVideoPreviewActivity.this,getApplicationContext().getPackageName()+".provider",fileur);
                            if (uri !=null){

                                Intent intent=new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                intent.setDataAndType(uri,"video/*");
                                intent.putExtra(Intent.EXTRA_STREAM,uri);

                                startActivity(Intent.createChooser(intent,"Share via"));

                            }else {
                                Toast.makeText(SavedVideoPreviewActivity.this, "Unable to display image.....", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }).start();
            }
        });
    }


    private void toogleFabs(boolean showIt) {
        int icon=R.drawable.ic_add;
        if (showIt){
            mBinding.icShareVideo.setVisibility(View.VISIBLE);
            mBinding.icDeleteVideo.setVisibility(View.VISIBLE);
            mBinding.icRepostVideo.setVisibility(View.VISIBLE);
            mBinding.repostTv.setVisibility(View.VISIBLE);
            mBinding.shareTv.setVisibility(View.VISIBLE);
            mBinding.delteTv.setVisibility(View.VISIBLE);
            icon=R.drawable.ic_cross;
        }else {
            mBinding.icShareVideo.setVisibility(View.GONE);
            mBinding.icDeleteVideo.setVisibility(View.GONE);
            mBinding.icRepostVideo.setVisibility(View.GONE);
            mBinding.repostTv.setVisibility(View.GONE);
            mBinding.shareTv.setVisibility(View.GONE);
            mBinding.delteTv.setVisibility(View.GONE);
            icon=R.drawable.ic_add;
        }
        mBinding.icAddFabSv.setImageDrawable(getResources().getDrawable(icon));



    }

    private void initRef() {
        mVideoView=findViewById(R.id.video_view_current_sv);
        full_sc_btn=findViewById(R.id.bt_full_screen);
        mPlaynext=findViewById(R.id.exo_next_btn);
        mPlayPrev=findViewById(R.id.exo_previous_);
    }

    private void getIntents() {

        Intent intent = getIntent();

        SavedModel selectedVideo = (SavedModel) intent.getSerializableExtra("selected_video");
        list= (List<SavedModel>) getIntent().getSerializableExtra("list");
        position=intent.getIntExtra("selected_video_position",-1);
        if (selectedVideo !=null){
            String path=selectedVideo.getPath();
            if (path!=null){
                setupPlayer(path);
            }
        }
    }

    private void setupPlayer(String path) {
        LoadControl loadControl=new DefaultLoadControl();
        TrackSelector trackSelector=new DefaultTrackSelector();
        mExoPlayer= ExoPlayerFactory.newSimpleInstance(SavedVideoPreviewActivity.this,trackSelector);

        DataSource.Factory datasource=new DefaultDataSourceFactory(this,"VideoPlayer");
        MediaSource videosource= new ExtractorMediaSource.Factory(datasource).createMediaSource(Uri.parse(path));
        mVideoView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.prepare(videosource);


        mVideoView.setKeepScreenOn(true);
        mExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState== Player.STATE_BUFFERING){
                    mBinding.progressBar.setVisibility(View.VISIBLE);

                }else if (playbackState==Player.STATE_READY){
                    mBinding.progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        full_sc_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (flag){
                    full_sc_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag=false;

                }else {
                    full_sc_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_screen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag=true;
                }
            }
        });
        mPlayPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previous = position - 1;
                if (previous < 0){
                    previous=list.size()-1;
                    position=list.size()-1;
                }

                if(previous >= 0){
                    SavedModel previousVideo = list.get(previous);
                    playVideo(previousVideo, previous);
                }

            }
        });
        mPlaynext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int next = position + 1;
                if (next >list.size()){
                    next=0;
                    position=0;
                }



                if (next >=list.size()){
                    next=0;
                    position=0;
                }

                if (next <= list.size()){
                    SavedModel nextVideo = list.get(next);
                    playVideo(nextVideo, next);
                }
            }
        });

    }

    private boolean isValid(File file) {

        String name=file.getName();
        String extension = name.substring(name.lastIndexOf('.'));

        if(extension.equalsIgnoreCase(".mp4") || extension.equalsIgnoreCase(".MPEG") || extension.equalsIgnoreCase(".AVI"))
            return true;

//

        return false;
    }

    private void playVideo(SavedModel video, int videoPosition) {
        if(video != null){
            String path = video.getPath();

            if(path != null && !TextUtils.isEmpty(path)){
                position = videoPosition;
                mExoPlayer.release();
                mExoPlayer = null;
                setupPlayer(path);
            }

        }

    }

    @Override
    public void onBackPressed() {

        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            if(mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }

        }
        mExoPlayer.release();
        mExoPlayer.stop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mExoPlayer.setPlayWhenReady(false);
        mExoPlayer.getPlaybackState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mExoPlayer=null;
        getIntents();
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.getPlaybackState();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mExoPlayer.release();
        mExoPlayer=null;
    }

}