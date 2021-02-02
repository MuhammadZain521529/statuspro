package com.muhammadzain.statuspro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.muhammadzain.statuspro.Activities.SavedGallery;
import com.muhammadzain.statuspro.Activities.VideoActivity;
import com.muhammadzain.statuspro.Constant.Constants;
import com.muhammadzain.statuspro.Models.VideoModel;
import com.muhammadzain.statuspro.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context mContext;
    private List<VideoModel> mList;

    public VideoAdapter(Context mContext, List<VideoModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_video_whatsapp,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoModel list=mList.get(position);
        File file=new File(list.getPath());
        Uri uri= Uri.fromFile(file);
        Glide
                .with(mContext)
                .load(uri)
                .into(holder.mVideoView);
        holder.mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra("list", (Serializable) mList);
                intent.putExtra("selected_video", list);
                intent.putExtra("selected_video_position", position);
                mContext.startActivity(intent);
            }
        });
        holder.mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = list.getPath();
                File file=new File(path);
                Uri uri_new= Uri.fromFile(file);

                Uri uri12 = Uri.parse(uri_new.toString());
                File fileur = new File(uri12.getPath());
                if (uri_new.toString().endsWith(".mp4")){


                    Uri uri= FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName()+".provider",fileur);
                    if (uri !=null){

                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.setDataAndType(uri,"video/*");
                        intent.putExtra(Intent.EXTRA_STREAM,uri);

                        mContext.startActivity(Intent.createChooser(intent,"Share via"));

                    }else {
                        Toast.makeText(mContext, "Unable to display image.....", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        holder.mSavetn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadImage(list,holder.itemView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void downloadImage(VideoModel list, View itemView) throws IOException {

        File files = new File(Constants.APP_DIR);
        if (!files.exists()){
            files.mkdirs();
        }
        File destfile=new File(files+ File.separator+list.getTitle());
        if (destfile.exists()){
            destfile.delete();
        }
        copyFile(list.getPath(),destfile);
        Snackbar.make(itemView,"Video Saved Successfully", Snackbar.LENGTH_LONG)
                .setAction("Open Saved Gallery", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, SavedGallery.class);
                        intent.putExtra("isWhatsappVideo",true);
                        mContext.startActivity(intent);
                    }
                }).show();
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setDataAndType(Uri.fromFile(destfile),"video/*");
        mContext.getApplicationContext().sendBroadcast(intent);
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
        source=new FileInputStream(file).getChannel();
        destination=new FileOutputStream(destfile).getChannel();
        destination.transferFrom(source,0,source.size());
        source.close();
        destination.close();

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView mVideoView;
        private ImageButton mPlayBtn,mSavetn,mShareBtn;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoView=itemView.findViewById(R.id.whatsapp_video);
            mPlayBtn=itemView.findViewById(R.id.play_video);
            mSavetn=itemView.findViewById(R.id.save_whatsapp_video);
            mShareBtn=itemView.findViewById(R.id.share_whatsapp_video);



        }
    }
}
