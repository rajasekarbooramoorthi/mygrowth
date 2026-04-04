package com.raj.mygrowth.networkUtility

import com.raj.mygrowth.domain.CategoryMasterResponse
import com.raj.mygrowth.domain.DailyTaskResponse
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAddTask
import com.raj.mygrowth.domain.RequestActionAndroidInterview
import com.raj.mygrowth.domain.RequestActionTaskCompleted
import com.raj.mygrowth.domain.ResponseAndroidInterview
import com.raj.mygrowth.domain.ResponseAndroidMaster
import com.raj.mygrowth.domain.ResponseBankDetails
import com.raj.mygrowth.domain.ResponseCareerItem
import com.raj.mygrowth.domain.ResponseCareerMaster
import com.raj.mygrowth.domain.ResponseFinance
import com.raj.mygrowth.domain.ResponseGeneric
import com.raj.mygrowth.domain.ResponsePassword
import com.raj.mygrowth.domain.ResponseSelfImprovement
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
    suspend fun getAndroidInterview(
        @Body request: RequestAction
    ): ResponseAndroidInterview

    @POST("getmydata.php")
    suspend fun insertInterviewQuestion(
        @Body request: RequestActionAndroidInterview
    ): ResponseAndroidInterview

    @POST("getmydata.php")
    suspend fun getAndroidMaster(
        @Body request: RequestAction
    ): ResponseAndroidMaster

    @POST("getmydata.php")
    suspend fun getSelfImprovement(
        @Body request: RequestAction
    ): ResponseSelfImprovement

    @POST("getmydata.php")
    suspend fun getAndroidMasterData(
        @Body request: RequestAction
    ): ResponseGeneric

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

    @POST("getmydata.php")
    suspend fun getCareerMaster(
        @Body request: RequestAction
    ): ResponseCareerMaster


    @POST("getmydata.php")
    suspend fun getCareerItem(
        @Body request: RequestAction
    ): ResponseCareerItem

    @POST("getmydata.php")
    suspend fun getMyGrowth(
        @Body request: RequestAction
    ): ResponseGeneric

    @GET("main.json")
    suspend fun getMaster(

    ): CategoryMasterResponse

    @GET("kural.json")
    suspend fun getThirukural(

    ): ThirukuralResponse
}