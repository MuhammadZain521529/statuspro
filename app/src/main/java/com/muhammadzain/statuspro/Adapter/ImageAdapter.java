package com.muhammadzain.statuspro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
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
import com.muhammadzain.statuspro.Activities.PreviewActivity;
import com.muhammadzain.statuspro.Activities.SavedGallery;
import com.muhammadzain.statuspro.Constant.Constants;
import com.muhammadzain.statuspro.Models.ModelImage;
import com.muhammadzain.statuspro.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyImageViewHolder> {

    public interface ChangeStatusListner{
        void onItemChangeListner(int position, ModelImage modelImage);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    private int selectedPosition = -1;

    private Context mContext;
    private List<ModelImage> mList;
    private ChangeStatusListner changeStatusListner;


    public ImageAdapter(Context mContext, List<ModelImage> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public ImageAdapter(Context mContext, List<ModelImage> mList, ChangeStatusListner changeStatusListner) {
        this.mContext = mContext;
        this.mList = mList;
        this.changeStatusListner = changeStatusListner;
    }

    private static final String ROOT_DIR= Environment.getExternalStorageDirectory().toString();
    public static final File PIC_FOLDER=new File(ROOT_DIR+"/Status Folder");
    private Bitmap bitmap;
    private static final String TAG = "ImageAdapter";
    private Serializable serializable;
    private boolean isLongClicked = false;



    @NonNull
    @Override
    public MyImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_images_whatsapp, parent, false);


        return new MyImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageViewHolder holder,   int position) {

         ModelImage list = mList.get(position);
        File file=new File(list.getImagePath());
        Uri uri= Uri.fromFile(file);
               Glide
                       .with(mContext)
                       .asBitmap()
                       .load(uri)
                       .into(holder.imageView);


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(mContext, PreviewActivity.class);
                            intent.putExtra("iv",(Serializable)list);
                            intent.putExtra("image",list.getImagePath());
                            mContext.startActivity(intent);
                        }
                    }).start();

                }
            });
            holder.mShareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file=new File(String.valueOf(list.getImagePath()));
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
            holder.mSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        downloadimage(list,holder.itemView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    private void downloadimage(ModelImage list, View itemView) throws IOException {

        File files = new File(Constants.APP_DIR);
        if (!files.exists()){
            files.mkdirs();
        }
        File destfile=new File(files+ File.separator+list.getTitle());
        if (destfile.exists()){
            destfile.delete();
        }
        copyFile(list.getImagePath(),destfile);
        Snackbar.make(itemView,"Image Saved Successfully", Snackbar.LENGTH_LONG)
                .setAction("Open Saved Gallery", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent=new Intent(mContext, SavedGallery.class);
                       intent.putExtra("dnimage",list.getImagePath());
                       mContext.startActivity(intent);
                    }
                }).show();


        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setDataAndType(Uri.fromFile(destfile),"image/*");
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
        source=new FileInputStream(String.valueOf(file)).getChannel();
        destination=new FileOutputStream(destfile).getChannel();
        destination.transferFrom(source,0,source.size());
        source.close();
        destination.close();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageButton mShareBtn,mSaveBtn;

        public MyImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.whatsapp_iv);
            mShareBtn=itemView.findViewById(R.id.share_whatsapp_iv);
            mSaveBtn=itemView.findViewById(R.id.save_whatsapp_iv);









        }



    }
}
