package com.example.final_535_app.view.video.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.final_535_app.R;
import com.example.final_535_app.model.BiliBiliVideo;
import com.example.final_535_app.view.video.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerItemNormalHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerItemNormalHolder";

    protected Context context = null;

    @BindView(R.id.video_item_player)
    SampleCoverVideo gsyVideoPlayer;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.user_photo_view)
    CircleImageView circleImageView;
    @BindView(R.id.tv_author)
    TextView tvAuthor;

    @BindView(R.id.page_video_thumb_count)
    TextView thumbCount;
    @BindView(R.id.video_comment)
    TextView commentCount;
    @BindView(R.id.tv_page_share)
    TextView shareCount;
    @BindView(R.id.tv_desc)
    TextView desc;

    ImageView imageView;

    GSYVideoOptionBuilder gsyVideoOptionBuilder;


    public RecyclerItemNormalHolder(Context context, View v) {
        super(v);
        this.context = context;
        ButterKnife.bind(this, v);
        imageView = new ImageView(context);
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    public void onBind(final int position, BiliBiliVideo videoModel) {

        String videoUrl = videoModel.getVideoUrl();
        String textContent = videoModel.getTitle();

        // 页面基本参数设置
        Glide.with(context).load(videoModel.getPic()).into(imageView);
        Glide.with(context).load(videoModel.getOwnerFace()).into(circleImageView);
        tvAuthor.setText(videoModel.getOwnerName());
        tvContent.setText(" " + textContent);
        thumbCount.setText(videoModel.getLike());
        commentCount.setText(videoModel.getDanmaku().toString());
        shareCount.setText(videoModel.getShare().toString());
        desc.setText(videoModel.getDesc());


        Map<String, String> header = new HashMap<>();
        header.put("ee", "33");

        // 防止错位，离开释放
        //gsyVideoPlayer.initUIState();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(imageView)
                .setUrl(videoUrl)
                .setVideoTitle(textContent)
                .setCacheWithPlay(true)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag(TAG)
                .setMapHeadData(header)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(position)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                }).build(gsyVideoPlayer);


        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });

//        gsyVideoPlayer.loadCoverImageBy(R.drawable.img_default_movie_h, R.drawable.img_default_movie_h);

    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }

    public SampleCoverVideo getPlayer() {
        return gsyVideoPlayer;
    }

}