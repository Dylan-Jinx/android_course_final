package com.example.final_535_app.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener


@Suppress("DEPRECATION")
class LocationUtil: AMapLocationListener {
    private var aMapLocationClient: AMapLocationClient? = null
    private var clientOption: AMapLocationClientOption? = null
    private var callBack: ILocationCallBack? = null

    fun startLocate(context: Context?) {
        aMapLocationClient = AMapLocationClient(context)
        Toast.makeText(context,"正在定位", Toast.LENGTH_SHORT).show()
        //设置监听回调
        aMapLocationClient!!.setLocationListener(this)
        //初始化定位参数
        clientOption = AMapLocationClientOption()
        clientOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        clientOption!!.isNeedAddress = true
        clientOption!!.isOnceLocation = true
        //设置是否强制刷新WIFI，默认为强制刷新
        clientOption!!.isWifiActiveScan = true
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        clientOption!!.isMockEnable = false
        //设置定位间隔
        clientOption!!.interval = 2000
        aMapLocationClient!!.setLocationOption(clientOption)

        aMapLocationClient!!.startLocation()
        Toast.makeText(context, "定位初始化成功", Toast.LENGTH_SHORT).show()
    }

    //完成定位回调
    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (aMapLocation != null) {
            if (aMapLocation.errorCode == 0) {
                //定位成功完成回调
                val country = aMapLocation.country
                val province = aMapLocation.province
                val city = aMapLocation.city
                val district = aMapLocation.district
                val street = aMapLocation.street
                val lat = aMapLocation.latitude
                val lgt = aMapLocation.longitude
                callBack!!.callBack(
                    country + province + city + district + street,
                    lat,
                    lgt,
                    aMapLocation
                )
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(
                    "AmapError", "location Error, ErrCode:"
                            + aMapLocation.errorCode + ", errInfo:"
                            + aMapLocation.errorInfo
                )
            }
        }
    }

    interface ILocationCallBack {
        fun callBack(str: String?, lat: Double, lgt: Double, aMapLocation: AMapLocation?)
    }

    fun setLocationCallBack(callBack: ILocationCallBack?) {
        this.callBack = callBack
    }
}