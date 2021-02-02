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

import com.muhammadzain.statuspro.Adapter.BusinessImageAdapter;
import com.muhammadzain.statuspro.Models.BusinessModelImage;
import com.muhammadzain.statuspro.databinding.FragmentBusinessImageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageBusinessFragment extends Fragment {
    private FragmentBusinessImageBinding mBinding;
    private BusinessImageAdapter adapter;
    private List<BusinessModelImage> photolist;
    private static final String TAG = "GBImageFragment";
    private static final String ROOT_DIR_GB = Environment.getExternalStorageDirectory().toString();
    private static final File PICTURE_GB = new File(ROOT_DIR_GB + "/WhatsApp Business/Media/.Statuses");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentBusinessImageBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generateList();
        swipeRecyclerView();

    }

    private void swipeRecyclerView() {

        mBinding.swipeRecyclerviewGb.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                generateList();
                adapter.notifyDataSetChanged();
                mBinding.swipeRecyclerviewGb.setRefreshing(false);
            }
        });


    }

    private void generateList() {
        photolist = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                File[] files;
                if (PICTURE_GB.isDirectory()) {
                    files = PICTURE_GB.listFiles();
                    Log.d("TAG", "run_File_gb: " + files);
                    if (files != null && files.length > 0) {

                        for (File myfile : files) {

                            if (!myfile.exists())
                                myfile.mkdirs();
                            if (isValidFile(myfile)) {
                                Log.d("TAG", "run_File: " + myfile);
                                Log.d("TAG", "run_File_Exists: " + myfile.exists());
                                Log.d("TAG", "run_File_Path: " + myfile.getAbsolutePath());

                                String imagepath = myfile.getAbsolutePath();
                                String title = myfile.getName();
                                File file = new File(imagepath);
                                BusinessModelImage image = new BusinessModelImage(imagepath, title, file);
                                photolist.add(image);
                            }
                        }
                    }

                }
            }
        }).start();
        setupRecyclerView(photolist);
        Log.d(TAG, "generateList:" + photolist);
    }

    private void setupRecyclerView(List<BusinessModelImage> photolist) {
        adapter = new BusinessImageAdapter(getContext(), photolist);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSmoothScrollbarEnabled(true);
        mBinding.recyclerViewGb.setLayoutManager(layoutManager);
        mBinding.recyclerViewGb.setAdapter(adapter);
        mBinding.recyclerViewGb.setHasFixedSize(true);

    }

    private boolean isValidFile(File file) {
        String name = file.getName();
        String extension = name.substring(name.lastIndexOf('.'));

        if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".JPEG"))
            return true;


        return false;
    }
}