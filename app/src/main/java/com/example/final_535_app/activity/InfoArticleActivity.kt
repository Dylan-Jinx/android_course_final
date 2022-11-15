package com.example.final_535_app.activity

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.example.final_535_app.databinding.ActivityInfoArticleBinding
import com.example.final_535_app.state.InfoArticleState
import com.example.final_535_app.viewmodel.InfoArticleViewModel

class InfoArticleActivity : AppCompatActivity(), MavericksView {

    lateinit var binding: ActivityInfoArticleBinding
    val infoArticleViewModel: InfoArticleViewModel by viewModel (InfoArticleViewModel::class)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var infoId = intent.getStringExtra("info_id")?.toInt()
        infoId?.let { infoArticleViewModel.getInfoArticle(it) }
        infoArticleViewModel.getInfoArticle(1)

        infoArticleViewModel.onAsync(
            InfoArticleState::noteDatas,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                invalidate()
            }, onFail = {
                Toast.makeText(this, "找不到当前文章信息", Toast.LENGTH_SHORT).show()
            }
        )

        binding.ivNoteBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun invalidate() = withState(infoArticleViewModel){ state ->
        val data = state.noteDatas.invoke()!!.data
        val context = binding.root.context

        Glide.with(context).load(data?.face).into(binding.infoFace)
        Glide.with(context).load(data?.bannerPic).into(binding.rcInfoBanner)

        binding.infoContent.text = data!!.content
        binding.tvInfoAuthor.text = data!!.username
        binding.tvInfoDesc.text = data!!.sign
        binding.infoContent.text = matcherSearchText(Color.BLUE,"\u3000\u3000"+data!!.content,"#")

        return@withState
    }

    fun matcherSearchText( color:Int,  string:String,  keyWord:String):CharSequence {
        var builder = SpannableStringBuilder(string);
        var indexOf = string.indexOf(keyWord);
        if (indexOf != -1) {
            builder.setSpan(ForegroundColorSpan(color), indexOf, indexOf + keyWord.length, SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;

    }
}