package com.example.final_535_app.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.final_535_app.databinding.ActivityMyVideoBinding
import com.example.final_535_app.fragment.HomeFragment
import com.example.final_535_app.fragment.MyDraftVideoFragment
import com.example.final_535_app.fragment.MyVideoFragment
import com.google.android.material.tabs.TabLayoutMediator

class MyVideoActivity : AppCompatActivity() {
    private val TAB_TITLES = arrayOf(
        "视频",
        "专栏",
        "音频"
    )
    private lateinit var binding: ActivityMyVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvent()

        binding.vg2MyVideo.adapter = object: FragmentStateAdapter(this){
            override fun getItemCount() = TAB_TITLES.size

            override fun createFragment(position: Int): Fragment {
                if (position == 0){
                    return MyDraftVideoFragment()
                }else{
                    return MyVideoFragment(position)
                }
            }
        }


        TabLayoutMediator(binding.tlMyVideoManager, binding.vg2MyVideo){ tab, position ->
            when (position) {
                0 -> tab.text = TAB_TITLES[position]
                1 -> tab.text = TAB_TITLES[position]
                2 -> tab.text = TAB_TITLES[position]
            }
        }.attach()

    }

    private fun initEvent() {
        binding.ivMyVideoBack.setOnClickListener{
            onBackPressed()
        }
    }
}