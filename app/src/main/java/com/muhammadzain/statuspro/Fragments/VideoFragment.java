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

import com.muhammadzain.statuspro.Adapter.VideoAdapter;
import com.muhammadzain.statuspro.Models.VideoModel;
import com.muhammadzain.statuspro.databinding.FragmentVideoBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VideoFragment extends Fragment {
    private FragmentVideoBinding mBinding;
    private static final String ROOT_DIR= Environment.getExternalStorageDirectory().toString();
    private static final File VIDEO_=new File(ROOT_DIR+"/WhatsApp/Media/.Statuses");
    private VideoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding=FragmentVideoBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipereccyclerview();
        generateList();

    }

    private void swipereccyclerview() {
        mBinding.swipeRecyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generateList();
                adapter.notifyDataSetChanged();
                mBinding.swipeRecyclerview.setRefreshing(false);

            }
        });


    }




    private void generateList() {

        List<VideoModel> list=new ArrayList<>();

                File[] files;
                if (VIDEO_.isDirectory()){
                    files=VIDEO_.listFiles();
                    if (files!=null && files.length>0){
                        for (File myvideo:files){
                            if (!myvideo.exists())
                                myvideo.mkdirs();
                            if (isInvalidFile(myvideo)){

                                if (myvideo.exists()){
                                    String title=myvideo.getName();
                                    String video=myvideo.getAbsolutePath();
                                    VideoModel listModel=new VideoModel(title,video);
                                    list.add(listModel);

                                }


                            }


                        }
                    }
                }

                setupRecyclerView(list);
    }

    private void setupRecyclerView(List<VideoModel> list) {
        adapter=new VideoAdapter(getContext(),list);
        mBinding.recyclerViewVideo.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        mBinding.recyclerViewVideo.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true);


    }

    private boolean isInvalidFile(File myvideo) {
        String name=myvideo.getName();
        String extension=name.substring(name.lastIndexOf('.'));
        if (extension.equalsIgnoreCase(".mp4") || (extension.equalsIgnoreCase(".MPEG")))
            return true;



        return false;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}