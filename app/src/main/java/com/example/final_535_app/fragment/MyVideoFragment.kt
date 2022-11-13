package com.example.final_535_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.final_535_app.R
import com.example.final_535_app.adapter.ItemFragmentRCMyVideoAdapter
import com.example.final_535_app.adapter.MyVideoAdapter
import com.example.final_535_app.databinding.FragmentMyVideoBinding
import com.example.final_535_app.state.MyVideoState
import com.example.final_535_app.viewmodel.MyVideoViewModel

class MyVideoFragment(var viewType: Int): Fragment(R.layout.fragment_my_video) {

    lateinit var binding: FragmentMyVideoBinding
    lateinit var myVideoAdapter: MyVideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyVideoBinding.inflate(layoutInflater)
        myVideoAdapter = MyVideoAdapter(viewType)
        return myVideoAdapter.onCreateViewHolder(binding.root, viewType).itemView
    }

}