package com.raj.mygrowth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raj.mygrowth.ActionString.POST_ADD_TASK
import com.raj.mygrowth.databinding.ActitvityAddSprintTaskBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAddSprintTask
import com.raj.mygrowth.interfaces.AdapterClick
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class AddSprintTaskActivity : AppCompatActivity(), AdapterClick {

    private lateinit var binding: ActitvityAddSprintTaskBinding
    private var sprintID = ""
    private val context = this@AddSprintTaskActivity
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarInsetsActivity()

        binding = ActitvityAddSprintTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerSprintHorizontal.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.btnSubmit.setOnClickListener {
            validate()
        }
        callApi()
    }

    private fun callApi() {
        viewModel.fetchSprint(RequestAction("get_sprint_master"))
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessSprintMaster -> {
                        val data = state.data

                        val adapter = SprintMasterAdapter(
                            data.data, this@AddSprintTaskActivity, this@AddSprintTaskActivity
                        )
                        binding.recyclerSprintHorizontal.adapter = adapter
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {}
                }
            }
        }
    }

    private fun addTask(request: RequestActionAddSprintTask) {
        viewModel.addSprintTask(request)
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessSprintTaskAdd -> {
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {}
                }
            }
        }
    }

    fun validate() {
        if (binding.editTextName.text.isNullOrEmpty()) {
            binding.editTextName.error = "is Empty"

        } else if (binding.editDescription.text.isNullOrEmpty()) {
            binding.editDescription.error = "is Empty"


        } else if (binding.editDetails.text.isNullOrEmpty()) {
            binding.editDetails.error = "is Empty"


        } else if (!binding.chkPriority.isChecked) {
            binding.chkPriority.error = "is Empty"

        } else if (sprintID.isEmpty()) {
            binding.chkPriority.error = "sprint is not selected"
        } else {
            addTask(
                RequestActionAddSprintTask(
                    action = POST_ADD_TASK,
                    name = binding.editTextName.text.toString().trim(),
                    description = binding.editDescription.text.toString().trim(),
                    details = binding.editDetails.text.toString().trim(),
                    priority = binding.chkPriority.isChecked.toString(),
                    id = sprintID
                )
            )

        }
    }

    override fun click(id: String) {
        sprintID = id
    }
}