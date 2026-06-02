package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.databinding.FragmentWorkoutBinding
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class WorkoutFragment : Fragment() {
    lateinit var dialogBottomSheetDialog: BottomSheetDialog
    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDailyTask.layoutManager = LinearLayoutManager(requireContext())
        dialogBottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)

        callApi()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun callApi() {
        viewModel.fetchWorkoutPlan()
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessWorkout -> {
                        val data = state.data
                        val adapter = WorkoutAdapter(data.workoutPlan, requireContext())
                        binding.rvDailyTask.adapter = adapter
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {}
                }
            }
        }
    }
}


