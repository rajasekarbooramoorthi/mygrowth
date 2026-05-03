package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.databinding.FragmentTodoBinding
import com.raj.mygrowth.databinding.FragmentWorkoutBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.WorkoutResponse
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class ToDoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        callApi()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun callApi() {
        viewModel.fetchAttendance(RequestAction("get_habit_todo_attendance"))
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()

                    }

                    is UiState.SuccessTodoAttendance -> {
                        val data = state.data.data
                        val adapter = AttendanceAdapter(data, requireContext())
                        binding.recyclerView.adapter = adapter
                    }

                    is UiState.Error -> {
                        // show error
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                    }

                    else -> {}
                }
            }
        }
    }
}


