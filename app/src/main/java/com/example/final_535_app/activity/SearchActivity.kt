package com.example.final_535_app.activity

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.GridLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.example.final_535_app.R
import com.example.final_535_app.adapter.SearchAdapter
import com.example.final_535_app.databinding.ActivitySearchBinding
import com.example.final_535_app.state.SearchState
import com.example.final_535_app.utils.KeyBoardUtil.closeKeyboard
import com.example.final_535_app.viewmodel.SearchViewModel

@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity(), MavericksView {

    lateinit var binding: ActivitySearchBinding
    val searchViewModel: SearchViewModel by viewModel(SearchViewModel::class)
    val handle:Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initSearchAnim()
        initEvent()

    }

    private fun initView() {
        binding.rvFilterResult.layoutManager = LinearLayoutManager(this)
    }

    private fun stopAnim() {
        var componentAnim = binding.ivSearchLoadingAnim.background as AnimationDrawable
        componentAnim.stop()
        binding.ivSearchLoadingAnim.visibility = View.INVISIBLE
        var layoutParams = binding.ivSearchLoadingAnim.layoutParams
        layoutParams.width = 0
        layoutParams.height = 0
        binding.rvFilterResult.visibility = View.VISIBLE
    }

    private fun startAnim() {
        binding.ivSearchLoadingAnim.visibility = View.VISIBLE
        var componentAnim = binding.ivSearchLoadingAnim.background as AnimationDrawable
        componentAnim.start()
        var layoutParams = binding.ivSearchLoadingAnim.layoutParams
        layoutParams.width = LayoutParams.WRAP_CONTENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        binding.rvFilterResult.visibility = View.INVISIBLE
    }

    private fun initSearchAnim() {
        binding.ivSearchLoadingAnim.visibility = View.INVISIBLE
        binding.ivSearchLoadingAnim.setBackgroundResource(R.drawable.search_loading_anim)
        var componentAnim = binding.ivSearchLoadingAnim.background as AnimationDrawable
        componentAnim.isOneShot = false
    }

    private fun initEvent() {
        binding.ivBackBtn.setOnClickListener{
            onBackPressed()
        }
        binding.tvSearchExecute.setOnClickListener{
            var filter:String = binding.etSearchFilter.text.toString()
            startAnim()
            searchViewModel.filterVideo(filter)
            handle.postDelayed({stopAnim()},2000)
        }



        binding.root.setOnClickListener{
            binding.rvFilterResult.visibility = View.VISIBLE
        }
        searchViewModel.onAsync(
            SearchState::filterData,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                invalidate()
            },
            onFail = {
            }
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.getAction() == MotionEvent.ACTION_DOWN) {
            var v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                var imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    if (v != null) {
                        closeKeyboard(v)
                    }//软键盘工具类
                    binding.etSearchFilter.clearFocus()
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        //必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            var editText= v
            var leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            var left = leftTop[0];
            var top = leftTop[1];
            var bottom = top + v.getHeight();
            var right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    override fun invalidate() = withState(searchViewModel){ state ->
        var searchResultAdapter = SearchAdapter(state.filterData.invoke()?.data)
        binding.rvFilterResult.adapter = searchResultAdapter
        return@withState
    }
}