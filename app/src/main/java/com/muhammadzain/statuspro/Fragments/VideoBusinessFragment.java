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
import com.muhammadzain.statuspro.Adapter.BusinessVideoAdapter;
import com.muhammadzain.statuspro.Models.BusinessModelVideo;
import com.muhammadzain.statuspro.databinding.FragmentBusinessVideoBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoBusinessFragment extends Fragment {
    private FragmentBusinessVideoBinding mBinding;
    private List<BusinessModelVideo> list;
    private static final String ROOT_DIR= Environment.getExternalStorageDirectory().toString();
    private static final File VIDEO_=new File(ROOT_DIR+"/WhatsApp Business/Media/.Statuses");
    private BusinessVideoAdapter adapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding=FragmentBusinessVideoBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generateList();
        swipereccyclerview();

    }

    @Override
    public void onStart() {
        super.onStart();
        generateList();
    }

    private void swipereccyclerview() {
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

         list=new ArrayList<>();

        File[] files;
        if (VIDEO_.isDirectory()){
            files=VIDEO_.listFiles();
            if (files!=null && files.length>0){
                for (File myvideo:files){
                    if (!myvideo.exists())
                        myvideo.mkdirs();
                    if (isInvalidFile(myvideo)){
                        String title=myvideo.getName();
                        String video=myvideo.getAbsolutePath();
                        BusinessModelVideo listModel=new BusinessModelVideo(title,video);
                        list.add(listModel);


                    }

                }
            }
        }

        setupRecyclerView(list);




    }

    private void setupRecyclerView(List<BusinessModelVideo> list) {
        adapter=new BusinessVideoAdapter(getContext(),list);
        mBinding.recyclerViewVideoGb.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        mBinding.recyclerViewVideoGb.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true);
    }

    private boolean isInvalidFile(File myvideo) {
        String name=myvideo.getName();
        String extension=name.substring(name.lastIndexOf('.'));
        if (extension.equalsIgnoreCase(".mp4") || (extension.equalsIgnoreCase(".MPEG")))
            return true;



        return false;

    }
}