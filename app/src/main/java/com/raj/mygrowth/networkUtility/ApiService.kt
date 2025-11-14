package com.raj.mygrowth.networkUtility

import com.raj.mygrowth.domain.DailyTaskResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("getmydata.php")
    @FormUrlEncoded
    suspend fun insertTask(
        @Field("action") taskName: String): DailyTaskResponse
}