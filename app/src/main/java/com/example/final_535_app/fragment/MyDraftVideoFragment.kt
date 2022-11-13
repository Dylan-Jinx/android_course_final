package com.example.final_535_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.final_535_app.R
import com.example.final_535_app.adapter.ItemFragmentRCMyVideoAdapter
import com.example.final_535_app.databinding.FragmentMyVideoBinding
import com.example.final_535_app.viewmodel.MyVideoViewModel

class MyDraftVideoFragment: Fragment(R.layout.fragment_my_video), MavericksView{

    lateinit var binding: FragmentMyVideoBinding
    val homeViewModel: MyVideoViewModel by fragmentViewModel(MyVideoViewModel::class)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyVideoBinding.inflate(layoutInflater)
        binding.rcFgMyVideoView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun invalidate() = withState(homeViewModel) { state ->
        var dataAdapter = state.videoInfos.invoke()?.data?.records?.let {
            ItemFragmentRCMyVideoAdapter(
                it
            )
        }
        binding.rcFgMyVideoView.adapter = dataAdapter
        return@withState
    }
}