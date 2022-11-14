package com.example.final_535_app.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.LatLng
import com.example.final_535_app.R
import com.example.final_535_app.utils.LocationUtil
import com.example.final_535_app.utils.LocationUtil.ILocationCallBack


class MapActivity : AppCompatActivity(), LocationSource {
    private var mapView: MapView? = null
    private var aMap: AMap? = null
    private var mListener: LocationSource.OnLocationChangedListener? = null //定位监听器
    private var locationUtil: LocationUtil? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView = findViewById<View>(R.id.map) as MapView
        mapView!!.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        if (aMap == null) {
            aMap = mapView!!.map
        }
        setLocationCallBack()

        //设置定位监听
        aMap!!.setLocationSource(this)
        //设置缩放级别
        aMap!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
        //显示定位层并可触发，默认false
        aMap!!.isMyLocationEnabled = true
    }

    private fun setLocationCallBack() {
        locationUtil = LocationUtil()
        locationUtil!!.setLocationCallBack(object : ILocationCallBack {
            override fun callBack(
                str: String?,
                lat: Double,
                lgt: Double,
                aMapLocation: AMapLocation?
            ) {

                //根据获取的经纬度，将地图移动到定位位置
                aMap!!.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(lat, lgt)))
                mListener?.onLocationChanged(aMapLocation)
            }
        })
    }

    //定位激活回调
    override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener?) {
        mListener = onLocationChangedListener
        locationUtil!!.startLocate(applicationContext)
    }

    override fun deactivate() {
        mListener = null
    }

    override fun onPause() {
        super.onPause()
        //暂停地图的绘制
        mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //销毁地图
        mapView!!.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //重新绘制加载地图
        mapView!!.onResume()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
}