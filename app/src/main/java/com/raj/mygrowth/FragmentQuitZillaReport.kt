package com.raj.mygrowth

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.adapter.QuitZillaReportAdapter
import com.raj.mygrowth.databinding.DialogQuitzillaMasterBinding
import com.raj.mygrowth.databinding.FragmentQuitZillaReportBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestQuitZillaMaster
import com.raj.mygrowth.domain.ResponseQuitZillaMasterItem
import com.raj.mygrowth.interfaces.AdapterClick
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentQuitZillaReport : Fragment(), AdapterClick {
    private var _binding: FragmentQuitZillaReportBinding? = null
    private val binding get() = _binding!!
    lateinit var dialogBottomSheetDialog: BottomSheetDialog
    var startDate: String? = null
    var endDate: String? = null
    var ID: String = ""

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(
            Repository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuitZillaReportBinding.inflate(inflater, container, false)
        dialogBottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        callApi()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun callApi() {
        viewModel.fetchQuitZillaReport(RequestAction("get_quit_zilla_master"))
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        // show loader
                        // Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.SuccessQuitZillaReport -> {
                        val list = state.data.data
                        val adapter = QuitZillaReportAdapter(
                            list, requireContext(), this@FragmentQuitZillaReport
                        )
                        binding.recyclerViewVertical.adapter = adapter
                        //Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun click(id: String) {
        // dialog(id)
    }

    override fun clickDetails(id: String, item: ResponseQuitZillaMasterItem) {
        dialog(id, item)
    }


    fun dialog(id: String, item: ResponseQuitZillaMasterItem) {
        ID = id
        var priority: String
        val binding = DialogQuitzillaMasterBinding.inflate(layoutInflater)
        dialogBottomSheetDialog.setContentView(binding.root)

        binding.icStartDate.setOnClickListener {
            showNormalDatePicker { date ->
                startDate = date
                binding.textStartDateValue.text = startDate
            }
        }
        binding.icEndDate.setOnClickListener {
            showNormalDatePicker { date ->
                endDate = date
                binding.textEndDateValue.text = endDate
            }
        }
        startDate = item.startDate
        endDate = item.endDate
        binding.apply {
            editTextName.setText(item.name)
            editTextNameDescription.setText(item.description)
            textStartDateValue.text = item.startDate
            textEndDateValue.text = item.endDate
            cbPriority.isChecked = item.priority == 0
        }
        dialogBottomSheetDialog.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                BottomSheetBehavior.from(sheet).apply {
                    state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    skipCollapsed = true
                }
                sheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        dialogBottomSheetDialog.show()

        binding.btnQuitZillaSubmit.setOnClickListener {

            val taskName = binding.editTextName.text
            val taskDescription = binding.editTextNameDescription.text
            if (!taskDescription.isNullOrEmpty() && !taskName.isNullOrEmpty() && endDate != null && startDate != null) {
                priority = if (binding.cbPriority.isChecked) {
                    "1"
                } else {
                    "0"
                }

                ID = if (binding.cbIsNew.isChecked) {
                    ""
                } else {
                    id
                }
                val requestAction = RequestQuitZillaMaster(
                    name = taskName.toString(),
                    description = taskDescription.toString(),
                    status = priority,
                    startDate = startDate.toString(),
                    endDate = endDate.toString(),
                    action = "Update_QuitZilla_Master",
                    id = ID
                )

                viewModel.updateQuitZillaMaster(requestAction)
                lifecycleScope.launch {
                    viewModel.uiState.collect { state ->
                        when (state) {

                            is UiState.Loading -> {
                                // show loader
                                // Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                            }

                            is UiState.SuccessQuitZillaUpdate -> {
                                binding.editTextName.text = null
                                binding.editTextNameDescription.text = null
                                binding.textStartDate.error = null
                                binding.textEndDate.error = null
                                endDate = null
                                startDate = null
                                callApi()
                                dialogBottomSheetDialog.dismiss()
                            }

                            is UiState.Error -> {
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            }

                            else -> {}
                        }
                    }
                }
            } else {
                if (binding.editTextNameDescription.text.isNullOrEmpty()) {
                    binding.editTextNameDescription.error = "should not Empty"
                }
                if (binding.editTextName.text.isNullOrEmpty()) {
                    binding.editTextName.error = "should not Empty"
                }
                if (startDate.isNullOrEmpty()) {
                    binding.textStartDate.error = "should select"
                }
                if (endDate.isNullOrEmpty()) {
                    binding.textEndDate.error = "should select"
                }
            }
        }

    }

    @SuppressLint("DefaultLocale")
    fun showNormalDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay
                )
                onDateSelected(formattedDate)
            }, year, month, day
        )
        datePicker.show()
    }

}