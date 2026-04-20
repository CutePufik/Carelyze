package com.example.carelyze.data.api

import com.example.carelyze.data.dto.AdviceDto
import com.example.carelyze.data.dto.AuthResponseDto
import com.example.carelyze.data.dto.UserLoginDto
import com.example.carelyze.data.dto.UserProfileResponseDto
import com.example.carelyze.data.dto.UserRegisterDto
import com.example.carelyze.data.dto.NnPredictionDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Body

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body userData: UserRegisterDto): Response<AuthResponseDto>

    @POST("auth/login")
    suspend fun login(@Body credentials: UserLoginDto): Response<AuthResponseDto>

    @GET("auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserProfileResponseDto>
    
    @GET("advice")
    suspend fun getAllAdvices(): Response<List<AdviceDto>>
    
    @GET("advice/{advice_id}")
    suspend fun getAdviceById(@Path("advice_id") adviceId: Int): Response<AdviceDto>

    @Multipart
    @POST("nn/brain-tumor/predict")
    suspend fun predictBrainTumor(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<NnPredictionDto>

    @Multipart
    @POST("nn/alzheimer/predict")
    suspend fun predictAlzheimer(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<NnPredictionDto>

    @Multipart
    @POST("nn/skin-disease/predict")
    suspend fun predictSkinDisease(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<NnPredictionDto>

    @Multipart
    @POST("nn/lung-xray/predict")
    suspend fun predictLungXray(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<NnPredictionDto>
}
