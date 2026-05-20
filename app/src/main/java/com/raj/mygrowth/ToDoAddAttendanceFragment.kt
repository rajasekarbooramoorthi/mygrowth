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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.ActionString.POST_ADD_ATTENDANCE
import com.raj.mygrowth.Utilities.getCurrentDate
import com.raj.mygrowth.databinding.FragmentTodoAddAttendanceBinding
import com.raj.mygrowth.databinding.FragmentTodoBinding
import com.raj.mygrowth.domain.AttendanceGetItem
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAddAttendance
import com.raj.mygrowth.interfaces.ClickAttendance
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class ToDoAddAttendanceFragment : Fragment(), ClickAttendance {
    private var _binding: FragmentTodoAddAttendanceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoAddAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        callGetAdd()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun callGetAdd() {
        viewModel.fetchGetAttendance(RequestAction(POST_ADD_ATTENDANCE))
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.SuccessGetAddAttendance -> {
                        val data = state.data.data
                        val adapter = AdapterGetAttendanceItem(data, this@ToDoAddAttendanceFragment)
                        binding.recyclerView.adapter = adapter
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

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

    fun callApiSendAttendance(request: RequestActionAddAttendance) {
        viewModel.addAttendance(
            RequestActionAddAttendance(
                "insert_daily_attendance",
                request.status,
                request.id,
                getCurrentDate()
            )
        )
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                        //Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.SuccessTodoAttendance -> {

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

    override fun click(request: RequestActionAddAttendance) {
        callApiSendAttendance(request)
    }
}


