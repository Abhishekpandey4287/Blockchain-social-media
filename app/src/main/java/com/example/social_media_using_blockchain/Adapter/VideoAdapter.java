package com.example.social_media_using_blockchain.Adapter;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.social_media_using_blockchain.R;
import com.example.social_media_using_blockchain.models.VideoModel;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private ArrayList<VideoModel> videoData;
    private ViewPager2 viewPager;

    public VideoAdapter(ArrayList<VideoModel> videoData, ViewPager2 viewPager) {
        this.videoData = videoData;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main, parent, false);
        return new MyViewHolder(view, viewPager);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(videoData.get(position));
    }

    @Override
    public int getItemCount() {
        return videoData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView songName, description, username;
        ProgressBar progressBar;
        ViewPager2 viewPager;

        public MyViewHolder(@NonNull View itemView, ViewPager2 viewPager) {
            //
            super(itemView);
            this.viewPager = viewPager;
            videoView = itemView.findViewById(R.id.videoView);
            songName = itemView.findViewById(R.id.songName);
            description = itemView.findViewById(R.id.description);
            username = itemView.findViewById(R.id.userName);
            progressBar = itemView.findViewById(R.id.videoProgressBar);
        }

        void bindData(VideoModel videoModel) {
            description.setText(videoModel.getDescription());
            username.setText(videoModel.getUserName());
            songName.setText(videoModel.getSongName());

            setUpVideoView(videoModel.getMedia_url());
        }

        private void setUpVideoView(String mediaUrl) {
            Uri videoUri = Uri.parse(mediaUrl);
            videoView.setVideoURI(videoUri);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
                    float targetRatio =9f /16f; // 9:15 aspect ratio

                    float scale;
                    if (videoRatio > targetRatio) {
                        // Fit width and crop top/bottom
                        scale = targetRatio / videoRatio;
                        videoView.setScaleX(scale);
                        videoView.setScaleY(1f);
                    } else {
                        // Fit height and crop sides
                        scale = videoRatio / targetRatio;
                        videoView.setScaleX(1f);
                        videoView.setScaleY(scale);
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                    mediaPlayer.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }
}