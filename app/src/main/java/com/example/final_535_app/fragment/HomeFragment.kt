package com.example.final_535_app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.final_535_app.activity.MessageActivity
import com.example.final_535_app.activity.SearchActivity
import com.example.final_535_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        initEvent()
        return view
    }

    private fun initEvent() {
        binding.llHomeSearch.setOnClickListener{
            startActivity(Intent(context, SearchActivity::class.java))
        }
        // 跳转信息页面
        binding.ivMyMessage.setOnClickListener{
            startActivity(Intent(context, MessageActivity::class.java))
        }
    }

}