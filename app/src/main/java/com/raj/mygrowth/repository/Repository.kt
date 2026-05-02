package com.raj.mygrowth.repository

import android.content.Context
import com.google.gson.Gson
import com.raj.mygrowth.domain.WorkoutResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository(private val context: Context) {

    private val gson = Gson()

    fun getWorkoutPlan(): Flow<WorkoutResponse> = flow {
        delay(500)

        val json = loadJSONFromAssets()
        val data = parseJson(json)

        emit(data)
    }.flowOn(Dispatchers.IO)

    private fun loadJSONFromAssets(): String {
        return context.assets.open("workout.json")
            .bufferedReader()
            .use { it.readText() }
    }

    private fun parseJson(json: String): WorkoutResponse {
        val response = gson.fromJson(json, WorkoutResponse::class.java)
        return response
    }
}