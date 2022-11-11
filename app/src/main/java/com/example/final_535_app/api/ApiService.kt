package com.example.final_535_app.api

import com.example.final_535_app.common.ApiResponse
import com.example.final_535_app.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
//    @GET("/problemBase/page?pageNum=1&pageSize=10")
//    suspend fun fetchProblemBase(): ApiResponse<ProblemBase>

    @POST("/appUser/login")
    suspend fun userLogin(@Body appUser: AppUser): ApiResponse<AppUser>

    @GET("/bilibiliUserInfo/findInfoByMid")
    suspend fun getUserInfo(@Query("mid") mid: Int): ApiResponse<BilibiliUserInfo>

    @GET("/minio")
    suspend fun getMinioFile(@Query("objectName") objectName: String): ApiResponse<Minio>

    @GET("/bilibiliUserInfo/page")
    suspend fun getUserInfoByPage(@Query("pageNum") pageNum: Number,@Query("pageSize") pageSize: Number): ApiResponse<PageDataModel<BilibiliUserInfo>>

    @GET("/bilibiliVideo/getVideoIntro")
    suspend fun getVideoInfo(@Query("pageNum") pageNum: Number,@Query("pageSize") pageSize: Number) : ApiResponse<PageDataModel<BiliBiliVideo>>

}