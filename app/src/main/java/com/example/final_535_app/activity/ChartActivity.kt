package com.example.final_535_app.activity

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.final_535_app.databinding.ActivityChartBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*


class ChartActivity : AppCompatActivity() {

    lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lineChart()
        pieChart()
    }

    private fun pieChart() {
        var entries:MutableList<PieEntry> = ArrayList()
        entries.add( PieEntry(18.5f, "Green"));
        entries.add( PieEntry(26.7f, "Yellow"));
        entries.add( PieEntry(24.0f, "Red"));
        entries.add( PieEntry(30.8f, "Blue"))
        var xVal = arrayListOf<String>("1","2","3","4","5","6","7")

        // 饼图设置
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setEntryLabelTextSize(16f)

        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setDrawCenterText(false)

        // 设置图列
        var l: Legend = binding.lcChart.legend
        l.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 10f
        l.setDrawInside(false)

        var pieDataSet: PieDataSet = PieDataSet(entries,"")
        pieDataSet.valueTextSize = 0f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.rgb(125, 25, 235))
        colors.add(Color.rgb(114, 188, 223))
        colors.add(Color.rgb(255, 123, 124))
        colors.add(Color.rgb(104, 188, 223))
        pieDataSet.colors = colors

        var pieData = PieData(pieDataSet)
        binding.pieChart.data = pieData
        binding.pieChart.invalidate()

    }

    private fun lineChart() {
        var dataObjects = arrayOf<YourData>(
            YourData(1,10),
            YourData(2,20),
            YourData(3,40),
            YourData(4,30),
            YourData(5,60),
            YourData(6,50),
            YourData(7,80),
        )
        var datas:MutableList<Entry> = ArrayList()
        for (dataObject in dataObjects) {
            // turn your data into Entry objects
            datas.add(Entry(dataObject.ValueX.toFloat(), dataObject.ValueY.toFloat()))
        }
        val dataSet = LineDataSet(datas,"")
        //是否有左下⾓的标签
        binding.lcChart.getLegend().form = Legend.LegendForm.NONE
        // 线段设置
        dataSet.color = Color.CYAN
        dataSet.lineWidth = 5f

        // 交接圆点
        dataSet.setDrawCircles(true)
        dataSet.setDrawValues(false)
        dataSet.setDrawCircleHole(true)
        dataSet.setCircleColors(Color.RED)
        dataSet.circleHoleColor = Color.WHITE

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
        val ValueX:Int,
        val ValueY: Int,
    )
}