package com.example.final_535_app.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.example.final_535_app.databinding.ActivityChartBinding
import com.example.final_535_app.state.ChartState
import com.example.final_535_app.viewmodel.ChartViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import java.text.DecimalFormat


@RequiresApi(Build.VERSION_CODES.O)
class ChartActivity : AppCompatActivity(), MavericksView {

    lateinit var binding: ActivityChartBinding
    val chartViewModel: ChartViewModel by viewModel(ChartViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lineChart(30)
        pieChart()
        radarChart()
        var bvid = intent.getStringExtra("data_center_bvid")
        if(!TextUtils.isEmpty(bvid)){
            bvid?.let { chartViewModel.getVideoDetail(it) }
            binding.llChartTopVideoInfo.setOnClickListener{
                startActivity(Intent(this, VideoDetailActivity::class.java).putExtra("bvid",bvid))
            }
        }
        initEvent()
    }

    fun generateFloatList(count:Int): MutableList<RadarEntry> {
        var cnt:Int = 0
        var dataObjects: MutableList<RadarEntry> = ArrayList()
        while(cnt < count){
            dataObjects.add(RadarEntry((1..100).random().toFloat()))
            cnt++
        }
        return dataObjects
    }

    private fun radarChart() {
        var radar = binding.radarChart

        radar.xAxis.textSize = 12f

        radar.yAxis.textSize = 0f
        radar.yAxis.textColor = Color.TRANSPARENT


        var radarDataSet = RadarDataSet(generateFloatList(5),"播放")
        var radarDataSet1 = RadarDataSet(generateFloatList(5),"点赞")
        var radarDataSet2 = RadarDataSet(generateFloatList(5),"投币")
        var radarDataSet3 = RadarDataSet(generateFloatList(5),"播放")
        var radarDataSet4 = RadarDataSet(generateFloatList(5),"分享")

        radarDataSet.valueTextSize = 0f
        radarDataSet1.valueTextSize = 0f
        radarDataSet2.valueTextSize = 0f
        radarDataSet3.valueTextSize = 0f
        radarDataSet4.valueTextSize = 0f

        radarDataSet.lineWidth = 3f
        radarDataSet1.lineWidth = 3f
        radarDataSet2.lineWidth = 3f
        radarDataSet3.lineWidth = 3f
        radarDataSet4.lineWidth = 3f

        radarDataSet.color = Color.argb(98,47, 128, 250)
        radarDataSet1.color = Color.argb(98,250, 195, 85)
        radarDataSet2.color = Color.argb(98,85, 60, 250)
        radarDataSet3.color = Color.argb(98,198, 250, 35)
        radarDataSet4.color = Color.argb(98,47, 128, 250)



        var radarData = RadarData(radarDataSet)
        radarData.addDataSet(radarDataSet1)
        radarData.addDataSet(radarDataSet2)
        radarData.addDataSet(radarDataSet3)
        radarData.addDataSet(radarDataSet4)
        radar.setData(radarData);

        //Y轴最小值不设置会导致数据中最小值默认成为Y轴最小值
        radar.yAxis.axisMinimum = 0f;
    }

    fun randomOpenTend(dayCount: Int): MutableList<YourData> {
        var dataObjects: MutableList<YourData> = ArrayList()
        var count:Int = 1
        while( count <= dayCount){
            dataObjects.add(YourData(count, (90..100).random()))
            count += 1
        }
        return dataObjects
    }

    private fun initEvent() {
        binding.ivDatacenterBack.setOnClickListener{
            onBackPressed()
        }
        binding.btnRelateSeven.setOnClickListener{
            lineChart(7)
        }
        binding.btnRelateThirty.setOnClickListener{
            lineChart(30)
        }
        chartViewModel.onAsync(
            ChartState::biliBiliVideo,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                invalidate()
            },
            onFail = {
                Toast.makeText(this, "网络开小差啦～", Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun generateFloatNumber(): Float {
        return 1 + (100 - 1) * ((1..100).random().toFloat()/(1..100).random().toFloat())
    }

    private fun pieChart() {
        var entries:MutableList<PieEntry> = ArrayList()
        entries.add( PieEntry(generateFloatNumber(), "移动端"));
        entries.add( PieEntry(generateFloatNumber(), "PC"));
        entries.add( PieEntry(generateFloatNumber(), "H5"));
        entries.add( PieEntry(generateFloatNumber(), "站外"))
        entries.add( PieEntry(generateFloatNumber(), "云视听小电视"))

        // 饼图设置
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setEntryLabelTextSize(0f)

        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setDrawCenterText(false)
        binding.pieChart

        // 设置图列
        var l: Legend = binding.lcChart.legend
        l.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.form = Legend.LegendForm.LINE
        l.formSize = 10f

        var pieDataSet: PieDataSet = PieDataSet(entries,"")
        pieDataSet.valueTextSize = 0f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.argb(98,193, 72, 250))
        colors.add(Color.argb(98,250, 195, 85))
        colors.add(Color.argb(98,85, 60, 250))
        colors.add(Color.argb(98,198, 250, 35))
        colors.add(Color.argb(98,47, 128, 250))
        pieDataSet.colors = colors
        pieDataSet.valueTypeface = Typeface.DEFAULT_BOLD

        var pieData = PieData(pieDataSet)
        binding.pieChart.data = pieData
        binding.pieChart.invalidate()

    }

    private fun lineChart(dayCount: Int) {
        var dataObjects = randomOpenTend(dayCount)
        Log.d("TAG", "randomOpenTend: "+dataObjects.size)
        var datas:MutableList<Entry> = ArrayList()
        for (dataObject in dataObjects) {
            // turn your data into Entry objects
            datas.add(Entry(dataObject.ValueX.toFloat(), dataObject.ValueY.toFloat()))
        }
        Log.d("TAG", "randomOpenTend: "+datas.size)
        val dataSet = LineDataSet(datas,"")
        //是否有左下⾓的标签
        binding.lcChart.getLegend().form = Legend.LegendForm.NONE
        // 线段设置
        dataSet.color = Color.CYAN
        dataSet.lineWidth = 5f
        //曲线下面填充颜色
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.CYAN)
        dataSet.fillAlpha = 12
        // 交接圆点
        dataSet.setDrawCircles(true)
        dataSet.setDrawValues(false)
        dataSet.setDrawCircleHole(true)
        dataSet.setCircleColors(Color.RED)
        dataSet.circleHoleColor = Color.WHITE
        dataSet.circleRadius = 5f
        // 坐标描述
        binding.lcChart.description.isEnabled = false
        // X Y轴
        val xAis = binding.lcChart.xAxis
        xAis.setDrawGridLines(false)
        xAis.textSize = 16f
        // X轴为底部
        xAis.position = XAxis.XAxisPosition.BOTTOM
        xAis.textColor = Color.GRAY
        val leftAis = binding.lcChart.axisLeft
        leftAis.setDrawGridLines(true)
        leftAis.setGridDashedLine(DashPathEffect(floatArrayOf(10f,10f,10f,10f,10f),1f))
        leftAis.textSize = 16f
        leftAis.textColor = Color.GRAY
        val rightAis = binding.lcChart.axisRight
        rightAis.setDrawGridLines(false)
        rightAis.isEnabled = false

        dataSet.mode = LineDataSet.Mode.LINEAR

        var lineData:LineData = LineData(dataSet)

        binding.lcChart.setNoDataText("无可用数据")


        // 禁止X Y 轴缩放
        binding.lcChart.isScaleXEnabled = false
        binding.lcChart.isScaleYEnabled = false
        // 禁止双击屏幕放大和高亮线
        binding.lcChart.isDoubleTapToZoomEnabled = false
        binding.lcChart.isHighlightPerDragEnabled = false


        binding.lcChart.data = lineData
        binding.lcChart.invalidate()
    }

    data class YourData(
        val ValueX: Int,
        val ValueY: Int,
    )

    override fun invalidate() = withState(chartViewModel) { state ->
        val data = state.biliBiliVideo.invoke()?.data

        Glide.with(binding.root.context)
            .load(data?.pic)
            .into(binding.rvSearchBanner)

        binding.tvChartVideoTitle.text = data?.title
        binding.tvChartVideoOpenInfo.text = data?.ctime

        binding.tvDatacenterView.text = data?.view.toString()
        binding.tvDatacenterCoin.text = data?.coin.toString()
        binding.tvDatacenterLike.text = data?.like
        binding.tvDatacenterDankamu.text = data?.danmaku.toString()
        binding.tvDatacenterCollect.text = data?.favorite.toString()
        binding.tvDatacenterShare.text = data?.share.toString()

    }
    private fun setVideoOpenInfo(data: Int?, tvHomeVideoOwner: TextView) {
        if(data!! > 10000){
            tvHomeVideoOwner.text = DecimalFormat("0.0").format(data/10000) + " 万"
        }else{
            tvHomeVideoOwner.text = data.toString()
        }
    }
}