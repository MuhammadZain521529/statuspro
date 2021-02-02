package com.muhammadzain.statuspro.Fragments;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.muhammadzain.statuspro.Adapter.SavedVideoAdapter;
import com.muhammadzain.statuspro.Models.SavedModel;
import com.muhammadzain.statuspro.databinding.FragmentSavedVideoBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class SavedVideoFragment extends Fragment {
    private FragmentSavedVideoBinding mBinding;
    private SavedVideoAdapter adapter;
    private static final String TAG = "SavedVideoFragment";
    private static final String ROOT_DIR= Environment.getExternalStorageDirectory().toString();
    private static final File STATUS_FOLDER=new File(ROOT_DIR+"/Status Pro");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentSavedVideoBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generatelist();
        swipeRecyclerview();

    }

    private void swipeRecyclerview() {
        mBinding.swipeSavedVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generatelist();
                adapter.notifyDataSetChanged();
                mBinding.swipeSavedVideo.setRefreshing(false);
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
                    if (!myFile.exists())
                       myFile.mkdirs();
                    if (isValid(myFile)){
                        String name=myFile.getName();
                        String image=myFile.getAbsolutePath();
                        SavedModel item=new SavedModel(name,image);
                        list.add(item);
                    }
                }

            }

        }

        bindList(list);
        adapter.notifyDataSetChanged();

    }

    private void bindList(List<SavedModel> list) {
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        mBinding.recyclerViewVideo.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true);
        adapter=new SavedVideoAdapter(list, getContext());
        mBinding.recyclerViewVideo.setAdapter(adapter);
        mBinding.recyclerViewVideo.setHasFixedSize(true);
    }

    private boolean isValid(File myFile) {
        String name=myFile.getName();
        String extention=name.substring(name.lastIndexOf('.'));
        Log.d(TAG, "isValid_extention: ");

        if(extention.equalsIgnoreCase(".mp4") || extention.equalsIgnoreCase(".mpeg") || extention.equalsIgnoreCase(".avi"))
            return true;

//

        return false;


    }

}