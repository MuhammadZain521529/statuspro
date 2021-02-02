package com.muhammadzain.statuspro.Fragments;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.muhammadzain.statuspro.Adapter.ImageAdapter;
import com.muhammadzain.statuspro.Models.ModelImage;
import com.muhammadzain.statuspro.databinding.FragmentImagesBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagesFragment extends Fragment{
    public boolean isActionMode=false;
    private FragmentImagesBinding mBinding;

    private ImageAdapter adapter;
    private List<ModelImage> photolist;
    private ConstraintLayout mLayout;
    private static final String ROOT_DIR= Environment.getExternalStorageDirectory().toString();
    private static final File PICTURE_=new File(ROOT_DIR+"/WhatsApp/Media/.Statuses");


    //private static final String[] IMAGES_EXTENSIONS = new String{};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mBinding=FragmentImagesBinding.inflate(inflater,container,false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        generateList();
        swipeRecyclerView();

    }



    private void swipeRecyclerView() {

        mBinding.swipeRecyclerviewIv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                generateList();
                adapter.notifyDataSetChanged();
                mBinding.swipeRecyclerviewIv.setRefreshing(false);

            }
        });



    }

    private void generateList() {
         photolist=new ArrayList<>();
                File[] files;
                if (PICTURE_.isDirectory()){
                    files=PICTURE_.listFiles();
                    Log.d("TAG", "run_File: "+files);
                    if (files != null &&files.length>0){
                        for (File myfile:files){
                                if (!myfile.exists())
                                    myfile.mkdirs();
                                if(isValidFile(myfile)){
                                    if (myfile.exists()){
                                        Log.d("TAG", "run_File_Exists: "+myfile.exists());
                                        Log.d("TAG", "run_File_Path: "+myfile.getAbsolutePath());
                                        String imagepathgb=myfile.getAbsolutePath();
                                        String title =myfile.getName();
                                        File file=new File(imagepathgb);
                                        ModelImage image=new ModelImage(imagepathgb,title,file);
                                        image.setSelected(false);
                                        photolist.add(image);


                                    }
                                }
                    }

                    }
                }


        setupRecyclerView(photolist);

    }
    private boolean isValidFile(File file) {
        Log.d("TAG", "isValidFile_Name: "+file.getName());

        String name=file.getName();
        String extension = name.substring(name.lastIndexOf('.'));

        if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".JPEG"))

            return true;

//

        return false;
    }

    private void setupRecyclerView(List<ModelImage> photolist) {
         adapter=new ImageAdapter(getContext(),photolist);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        layoutManager.setSmoothScrollbarEnabled(true);
        mBinding.recyclerViewIv.setLayoutManager(layoutManager);
        mBinding.recyclerViewIv.setAdapter(adapter);
        mBinding.recyclerViewIv.setHasFixedSize(true);

    }


}
