package com.raj.mygrowth.networkUtility

import com.raj.mygrowth.domain.CategoryMasterResponse
import com.raj.mygrowth.domain.DailyTaskResponse
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAddTask
import com.raj.mygrowth.domain.RequestActionTaskCompleted
import com.raj.mygrowth.domain.ResponseAttendance
import com.raj.mygrowth.domain.ResponseBankDetails
import com.raj.mygrowth.domain.ResponseFinance
import com.raj.mygrowth.domain.ResponsePassword
import com.raj.mygrowth.domain.ResponseSimple
import com.raj.mygrowth.domain.ThirukuralResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("getmydata.php")
    suspend fun getPassword(
        @Body request: RequestAction
    ): ResponsePassword

    @POST("getmydata.php")
    suspend fun getBankDetails(
        @Body request: RequestAction
    ): ResponseBankDetails

    @POST("getmydata.php")
    suspend fun getFinanceMasterData(
        @Body request: RequestAction
    ): ResponseFinance

    @POST("getmydata.php")
    suspend fun addTask(
        @Body request: RequestActionAddTask
    ): ResponseSimple

    @POST("getmydata.php")
    suspend fun getTaskDetails(
        @Body request: RequestAction
    ): DailyTaskResponse

    @POST("getmydata.php")
    suspend fun setStatusCompleted(
        @Body request: RequestActionTaskCompleted
    ): ResponseSimple


    @GET("main.json")
    suspend fun getMaster(): CategoryMasterResponse

    @GET("kural.json")
    suspend fun getThirukural(

    ): ThirukuralResponse


    @POST("getmydata.php")
    suspend fun getAttendance(
        @Body request: RequestAction
    ): ResponseAttendance

}