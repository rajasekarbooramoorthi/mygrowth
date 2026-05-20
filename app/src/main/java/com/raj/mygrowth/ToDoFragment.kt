package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.Utilities.getCurrentDate
import com.raj.mygrowth.databinding.FragmentTodoBinding
import com.raj.mygrowth.domain.AttendanceGetItem
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAddAttendance
import com.raj.mygrowth.interfaces.ClickAttendance
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class ToDoFragment : Fragment(), ClickAttendance {
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
        setupMenu()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        callApiReport()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun callApiReport() {
        viewModel.fetchAttendance(RequestAction("get_habit_todo_attendance"))
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                        //Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()

                    }

                    is UiState.SuccessTodoAttendance -> {
                        val data = state.data.data
                        val adapter = AttendanceReportAdapter(data, requireContext())
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
                        val data = state.data.data
                        val adapter = AttendanceReportAdapter(data, requireContext())
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

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.todo_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_add -> {
                        callApiAddAttendance()
                        true
                    }

                    else -> false
                }
            }
        })
    }


    fun dialog(list: List<AttendanceGetItem>) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        dialog.setContentView(R.layout.add_attendance)
        dialog.setCancelable(true)

        dialog.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                BottomSheetBehavior.from(sheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
                sheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                sheet.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        val rvDialog = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        rvDialog?.layoutManager = LinearLayoutManager(requireContext())
        rvDialog?.adapter = AdapterGetAttendanceItem(list, this)
        dialog.show()
    }

    fun callApiAddAttendance() {
        viewModel.fetchGetAttendance(
            RequestAction(
                "get_add_attendance")
        )
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // show loader
                        //Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.SuccessGetAddAttendance -> {
                        dialog(state.data.data)
                    }

                    is UiState.Error -> {
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


