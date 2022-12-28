package com.example.final_535_app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.example.final_535_app.adapter.MessageChatAdapter
import com.example.final_535_app.databinding.ActivityMessageBinding
import com.example.final_535_app.state.MessageChatState
import com.example.final_535_app.viewmodel.MessageChatViewModel

@Suppress("DEPRECATION")
class MessageActivity : AppCompatActivity(), MavericksView {

    lateinit var binding: ActivityMessageBinding
    val messageChatViewModel: MessageChatViewModel by viewModel(MessageChatViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        messageChatViewModel.messageChats(1, 10)

        messageChatViewModel.onAsync(
            MessageChatState::messageChats,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                binding.rcMessageChat.layoutManager = LinearLayoutManager(this)
                var messageChatAdapter = it.data?.records?.let { it1 -> MessageChatAdapter(it1) }
                binding.rcMessageChat.adapter = messageChatAdapter
            },
            onFail = {
                Toast.makeText(baseContext, "网络开小差啦～", Toast.LENGTH_SHORT).show()
            }
        )

        binding.ivMessageBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun invalidate() = withState(messageChatViewModel) {

    }
}