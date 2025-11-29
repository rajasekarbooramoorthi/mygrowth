package com.raj.mygrowth.networkUtility

import com.raj.mygrowth.domain.DailyTaskResponse
import com.raj.mygrowth.domain.ResponseAndroidInterview
import com.raj.mygrowth.domain.ResponseBankDetails
import com.raj.mygrowth.domain.ResponsePassword
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("getmydata.php")
    @FormUrlEncoded
    suspend fun insertTask(
        @Field("action") taskName: String
    ): DailyTaskResponse

    @POST("getmydata.php")
    @FormUrlEncoded
    suspend fun getPassword(
        @Field("action") taskName: String
    ): ResponsePassword

    @POST("getmydata.php")
    @FormUrlEncoded
    suspend fun getBankDetails(
        @Field("action") taskName: String
    ): ResponseBankDetails
    @POST("getmydata.php")
    @FormUrlEncoded
    suspend fun getAndroidInterview(
        @Field("action") taskName: String
    ): ResponseAndroidInterview
}