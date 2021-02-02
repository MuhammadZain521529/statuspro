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
import com.muhammadzain.statuspro.Activities.SavedGallery;
import com.muhammadzain.statuspro.Activities.SavedVideoPreviewActivity;
import com.muhammadzain.statuspro.Models.SavedModel;
import com.muhammadzain.statuspro.R;
import java.io.File;
import java.io.Serializable;
import java.util.List;

public class SavedVideoAdapter extends RecyclerView.Adapter<SavedVideoAdapter.MySavedVideoHolder> {
    private List<SavedModel> mList;
    private Context mContext;

    public SavedVideoAdapter(List<SavedModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MySavedVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_saved_video, parent, false);
        return new MySavedVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySavedVideoHolder holder, int position) {
        SavedModel list=mList.get(position);


        File file=new File(list.getPath());
        Uri uri= Uri.fromFile(file);
        Glide
                .with(mContext)
                .load(uri)
                .into(holder.mVideoView);
        holder.mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, SavedVideoPreviewActivity.class);
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
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = list.getPath();
                final File file = new File(String.valueOf(path));
                try {
                    if (file.exists()) {
                        file.delete();
                        Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(mContext, SavedGallery.class);
                        intent.putExtra("isDeleteVideo",true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MySavedVideoHolder extends RecyclerView.ViewHolder {
        private ImageView mVideoView;
        private ImageButton mPlayBtn,mDeleteBtn,mShareBtn;
        public MySavedVideoHolder(@NonNull View itemView) {
            super(itemView);
            mVideoView=itemView.findViewById(R.id.saved_video);
            mPlayBtn=itemView.findViewById(R.id.play_saved_video);
            mShareBtn=itemView.findViewById(R.id.share_saved_video);
            mDeleteBtn=itemView.findViewById(R.id.delete_saved_video);
        }
    }
}
