package com.example.final_535_app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.example.final_535_app.databinding.ActivityViewPagerVideoBinding
import com.example.final_535_app.state.ViewPagerVideoState
import com.example.final_535_app.view.video.holder.RecyclerItemNormalHolder
import com.example.final_535_app.viewmodel.ViewPageVideoViewModel
import com.example.instantmusicvideotest.adapter.ViewPagerVideoAdapter
import com.shuyu.gsyvideoplayer.GSYVideoManager


class ViewPagerVideoActivity : AppCompatActivity(), MavericksView {

    lateinit var binding: ActivityViewPagerVideoBinding
    val videoViewModel: ViewPageVideoViewModel by viewModel(ViewPageVideoViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvent()

    }

    private fun initEvent() {
        videoViewModel.onAsync(
            ViewPagerVideoState::biliBiliVideo,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                var viewPagerAdapter = ViewPagerVideoAdapter(this, it.data?.records)
                binding.viewPagerVideo.orientation = ViewPager2.ORIENTATION_VERTICAL
                binding.viewPagerVideo.adapter = viewPagerAdapter

                binding.viewPagerVideo.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // 大于0说明有播放
                        val playPosition = GSYVideoManager.instance().playPosition
                        if (playPosition >= 0) {
                            // 对应的播放列表TAG
                            if (GSYVideoManager.instance().playTag == RecyclerItemNormalHolder.TAG && position != playPosition) {
                                playPosition(position)
                            }
                        }
                    }
                })
                binding.viewPagerVideo.post(object :Runnable{
                    override fun run() {
                        playPosition(0)
                    }

                })

            }, onFail = {

            }
        )
    }


    private fun playPosition(position: Int) {
        val viewHolder =
            (binding.viewPagerVideo.getChildAt(0) as RecyclerView).findViewHolderForAdapterPosition(position)
        if (viewHolder != null) {
            val recyclerItemNormalHolder: RecyclerItemNormalHolder =
                viewHolder as RecyclerItemNormalHolder
            recyclerItemNormalHolder.getPlayer().startPlayLogic()
        }
    }

    override fun invalidate() = withState(videoViewModel){ state ->

    }


}