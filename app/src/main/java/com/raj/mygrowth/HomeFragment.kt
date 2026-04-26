package com.raj.mygrowth

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.databinding.BottomDialogAddTaskBinding
import com.raj.mygrowth.databinding.FragmentHomeBinding
import com.raj.mygrowth.domain.DailyTask
import com.raj.mygrowth.domain.DailyTaskItem
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionAddTask
import com.raj.mygrowth.domain.RequestActionTaskCompleted
import com.raj.mygrowth.interfaces.SimpleClick
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : Fragment(), SimpleClick {
    lateinit var dialogBottomSheetDialog: BottomSheetDialog
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var dueDate: String? = null

    val api = RetrofitClient.instance.create(ApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().setToolbarInsetsFragment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDailyTask.layoutManager = LinearLayoutManager(requireContext())
        dialogBottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        binding.fabAdd.setOnClickListener {
            dialog()
        }
        loadDailyTasks()
    }

    private fun loadDailyTasks() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = api.getTaskDetails(RequestAction("get_daily_task"))

                binding.progressBar.visibility = View.GONE

                if (response.status) {

                    val processedData = ArrayList<DailyTask>()

                    response.data.forEach { task ->
                        if (task.list.isNotEmpty()) {
                            processedData.add(DailyTask(task.taskName, task.list))
                        }
                    }
                    val adapter =
                        DailyTaskAdapter(processedData, this@HomeFragment, requireContext())
                    binding.rvDailyTask.adapter = adapter
                    var position = 0
                    response.data.forEachIndexed { index, item ->
                        if (item.taskName == "today") {
                            position = index
                        }
                        println("Index = $index, Item = $item")
                    }

                    binding.rvDailyTask.post {
                        binding.rvDailyTask.smoothScrollToPosition(position)
                    }
                } else {
                    Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("DefaultLocale")
    fun showNormalDatePicker(onDateSelected: (String) -> Unit) {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                val formattedDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )

                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    fun completed(id: String) {
        lifecycleScope.launch {
            if (api.setStatusCompleted(RequestActionTaskCompleted("update_task", id)).status) {
                lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(),
                        "Task completed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun dialog() {
        var priority: String
        val binding = BottomDialogAddTaskBinding.inflate(layoutInflater)
        dialogBottomSheetDialog.setContentView(binding.root)

        binding.icDate.setOnClickListener {
            showNormalDatePicker { date ->
                dueDate = date
            }
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

        binding.btnSubmit.setOnClickListener {

            val taskName = binding.editTextName.text
            val taskDescription = binding.editTextNameDescription.text
            if (!taskDescription.isNullOrEmpty() && !taskName.isNullOrEmpty() && dueDate != null) {
                priority = if (binding.cbPriority.isChecked) {
                    "1"
                } else {
                    "0"
                }

                val requestAction = RequestActionAddTask(
                    taskName = taskName.toString(),
                    description = taskDescription.toString(),
                    priority = priority,
                    dueDate = dueDate.toString(),
                    action = "insert_daily_task"
                )
                lifecycleScope.launch {
                    if (api.addTask(requestAction).status) {
                        binding.editTextName.text = null
                        binding.editTextNameDescription.text = null
                        binding.textDate.error = null
                        dueDate = null
                        lifecycleScope.launch {
                            Toast.makeText(
                                requireContext(),
                                "Task added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialogBottomSheetDialog.dismiss()
                        }
                    }
                }
                loadDailyTasks()
            } else {
                if (binding.editTextNameDescription.text.isNullOrEmpty()) {
                    binding.editTextNameDescription.error = "should not Empty"
                }
                if (binding.editTextName.text.isNullOrEmpty()) {
                    binding.editTextName.error = "should not Empty"
                }
                if (dueDate.isNullOrEmpty()) {
                    binding.textDate.error = "should select"
                }
            }
        }

    }

    override fun click(id: String, path: String, type: String) {

    }

    override fun clickChild(list: List<String>) {

    }

    override fun clickUrl(url: String) {

    }

    override fun checkCompleted(id: String) {
        completed(id)
    }

}


