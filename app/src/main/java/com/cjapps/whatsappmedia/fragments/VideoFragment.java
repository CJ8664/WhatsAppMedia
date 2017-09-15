package com.cjapps.whatsappmedia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cjapps.whatsappmedia.R;
import com.cjapps.whatsappmedia.utils.Video;

import java.util.ArrayList;
import java.util.Arrays;

public class VideoFragment extends Fragment {

    private ArrayList<Video> videosArrayList = new ArrayList<>();
    private RecyclerView videoRecyclerView;

    private boolean isSelectionActive;
    private ArrayList<Integer> selectedVideosPositions = new ArrayList<>();
    private ActionMode.Callback videoActionMode = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.navigationdraweritems, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.video_search_action: {

                    // Close the actionmode
                    actionMode.finish();

                    int selectedPostion = -1;
                    Object[] selectedPostions = selectedVideosPositions.toArray();
                    Arrays.sort(selectedPostions);

                    Log.e("selected pos arry", Arrays.toString(selectedPostions));

                    //for (int i = 0; i < selectedVideosPositions.size(); i++) {
                    for (int i = selectedPostions.length - 1; i >= 0; i--) {

                        selectedPostion = (int) selectedPostions[i];
                        Video video = videosArrayList.get(selectedPostion);

                        Log.e("will remove", video.getTitle() + " " + selectedPostion);

                        videosArrayList.remove(video);
                        videoRecyclerView.getAdapter().notifyItemRemoved(selectedPostion);

                    }
                    selectedVideosPositions.clear();
                    for (Video video : videosArrayList) {
                        video.setIsSelected(false);
                    }
                    return true;
                }
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isSelectionActive = false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoRecyclerView = (RecyclerView) view.findViewById(R.id.video_recyclerview);

        videoRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //mLayoutManager = new GridLayoutManager(getActivity(),4);
        videoRecyclerView.setLayoutManager(mLayoutManager);

        for (int i = 0; i < 100; i++) {
            Video temp = new Video();
            temp.setTitle("Title + " + i);
            videosArrayList.add(temp);
        }

        RecyclerView.Adapter mAdapter = new VideoRecyclerAdapter(videosArrayList);
        videoRecyclerView.setAdapter(mAdapter);

    }


    //-------------------------- Custom ViewHolder -----------------------------//

    private void selectCrime(Video v) {
        // start an instance of CrimePagerActivity

        // NOTE: shared element transition here.
        // Support library fragments do not support the three parameter
        // startActivityForResult call. So to get this to work, the entire
        // project had to be shifted over to use stdlib fragments,
        // and the v13 ViewPager.
        int index = videosArrayList.indexOf(v);
        VideoViewHolder holder = (VideoViewHolder) videoRecyclerView
                .findViewHolderForAdapterPosition(index);

        Toast.makeText(getActivity(), holder.txtHeader.getText(), Toast.LENGTH_SHORT).show();

    }

    //---------------------------- Action Mode -----------------------//

    public class VideoViewHolder extends ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public TextView txtHeader;
        public TextView txtFooter;
        public View parentView;

        private Video video;

        public VideoViewHolder(View parentView) {
            super(parentView);
            this.parentView = parentView;
            txtHeader = (TextView) parentView.findViewById(R.id.textView);
            txtFooter = (TextView) parentView.findViewById(R.id.textView2);
            parentView.setOnClickListener(this);
            parentView.setOnLongClickListener(this);
            parentView.setLongClickable(true);
            itemView.setClickable(true);
        }

        public void bindCrime(Video video) {
            this.video = video;
            txtHeader.setText(video.getTitle());
            txtFooter.setText(video.getTitle() + " Footer " + getAdapterPosition());
            parentView.setSelected(true);
        }

        @Override
        public void onClick(View v) {

            if (video == null) {
                return;
            }
            if (isSelectionActive) {
                if (!video.isSelected()) {
                    parentView.setSelected(true);
                    video.setIsSelected(true);
                    selectedVideosPositions.add(getAdapterPosition());
                    Log.e("selected", video.getTitle() + ", " + getAdapterPosition());
                } else {
                    parentView.setSelected(false);
                    video.setIsSelected(false);
                    selectedVideosPositions.remove(Integer.valueOf(getAdapterPosition()));
                    Log.e("unselected", video.getTitle() + ", " + getAdapterPosition());
                }
            } else {
                Log.e("clicked", video.getTitle() + ", " + getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {

            if (!isSelectionActive) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.startSupportActionMode(videoActionMode);
                isSelectionActive = true;

                video.setIsSelected(true);
                selectedVideosPositions.add(getAdapterPosition());
                Log.e("selected", video.getTitle() + ", " + getAdapterPosition());
            }
            return true;
        }
    }

    //------------------------------------ VideoHolder --------------------//

    public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoViewHolder> {

        private ArrayList<Video> videoArrayList;

        public VideoRecyclerAdapter(ArrayList<Video> videoArrayList) {
            this.videoArrayList = videoArrayList;
        }

        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_listitem, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int pos) {
            Video video = videoArrayList.get(pos);
            holder.bindCrime(video);
        }

        @Override
        public int getItemCount() {
            return videoArrayList.size();
        }
    }

}
