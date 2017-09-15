package com.cjapps.whatsappmedia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjapps.whatsappmedia.R;
import com.cjapps.whatsappmedia.adapters.ImageRecyclerAdapter;

public class ImageFragment extends Fragment {

    private RecyclerView imageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageRecyclerView = (RecyclerView) view.findViewById(R.id.image_recyclerview);
        imageRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        imageRecyclerView.setLayoutManager(mLayoutManager);

        //imageRecyclerView.setItemAnimator(new DefaultItemAnimator());



        RecyclerView.Adapter mAdapter = new ImageRecyclerAdapter(getActivity());
        imageRecyclerView.setAdapter(mAdapter);

    }

}
