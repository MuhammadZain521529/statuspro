package com.muhammadzain.statuspro.Fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.muhammadzain.statuspro.Adapter.SavedImageAdapter;
import com.muhammadzain.statuspro.Models.SavedModel;
import com.muhammadzain.statuspro.databinding.FragmentSavedImageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SavedImageFragment extends Fragment {
    private FragmentSavedImageBinding mBinding;
    private SavedImageAdapter adapter;
    private static final String ROOT_DIR= Environment.getExternalStorageDirectory().toString();
    private static final File STATUS_FOLDER=new File(ROOT_DIR+"/Status Pro");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentSavedImageBinding.inflate(inflater,container,false);
        return mBinding.getRoot();


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generatelist();
        swipeRecyclerView();




    }

    private void swipeRecyclerView() {
        mBinding.swipeSavedImages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generatelist();
                adapter.notifyDataSetChanged();
                mBinding.swipeSavedImages.setRefreshing(false);
            }
        });

    }


    private void generatelist() {
        List<SavedModel> list=new ArrayList<>();
        File[] files;


        if (STATUS_FOLDER.isDirectory()){
            files=STATUS_FOLDER.listFiles();
            if (files!=null){
                for (File myFile:files){
                    if (myFile.exists()){
                        if (isValid(myFile)){
                            String name=myFile.getName();
                            String image=myFile.getAbsolutePath();
                            SavedModel item=new SavedModel(name,image);
                            list.add(item);
                        }

                    }
                }

            }

        }

        bindList(list);
        adapter.notifyDataSetChanged();


    }

    private boolean isValid(File myFile) {
        String name=myFile.getName();
        String extention=name.substring(name.lastIndexOf('.'));

        if (extention.equalsIgnoreCase(".jpg")||extention.equalsIgnoreCase(".png")||extention.equalsIgnoreCase(".JPEG"))
        return true;

        return false;
    }

    private void bindList(List<SavedModel> list) {

        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        mBinding.recyclerViewImageSv.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true);
        adapter=new SavedImageAdapter(getContext(),list);
        mBinding.recyclerViewImageSv.setAdapter(adapter);
        mBinding.recyclerViewImageSv.setHasFixedSize(true);
    }
}