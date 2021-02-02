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
import com.muhammadzain.statuspro.Activities.PreviewSavedActivity;
import com.muhammadzain.statuspro.Activities.SavedGallery;
import com.muhammadzain.statuspro.Models.SavedModel;
import com.muhammadzain.statuspro.R;

import java.io.File;
import java.util.List;

public class SavedImageAdapter extends RecyclerView.Adapter<SavedImageAdapter.SavedViewHolder> {
    private Context mContext;
    private List<SavedModel> mList;

    public SavedImageAdapter(Context mContext, List<SavedModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_saved_images, parent, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        SavedModel list=mList.get(position);
        Glide
                .with(mContext)
                .load(list.getPath())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PreviewSavedActivity.class);
                intent.putExtra("savedimage",list);
                mContext.startActivity(intent);
            }
        });
        holder.mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(String.valueOf(list.getPath()));
                Uri uri_new= Uri.fromFile(file);

                Uri uri12 = Uri.parse(uri_new.toString());
                File fileur = new File(uri12.getPath());
                if (uri_new.toString().endsWith(".jpg")){


                    Uri uri= FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName()+".provider",fileur);
                    if (uri !=null){


                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.setDataAndType(uri,"image/*");
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

    public static class SavedViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton mShareBtn,mDeleteBtn;
       

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.saved_iv);
            mShareBtn=itemView.findViewById(R.id.share_saved_iv);
            mDeleteBtn=itemView.findViewById(R.id.delete_saved_iv);



        }
    }
}
