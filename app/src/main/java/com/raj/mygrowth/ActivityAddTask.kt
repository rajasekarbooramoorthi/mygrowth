package com.raj.mygrowth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raj.mygrowth.databinding.ActivityAddTaskBinding

class ActivityAddTask : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}