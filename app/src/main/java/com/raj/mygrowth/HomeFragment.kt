package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raj.mygrowth.databinding.FragmentHomeBinding
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDailyTask.layoutManager = LinearLayoutManager(requireContext())

        loadDailyTasks()
    }

    private fun loadDailyTasks() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.insertTask("get_daily_task")

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    val adapter = DailyTaskAdapter(response.data)
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
                    Toast.makeText(requireContext(), "Data Loaded", Toast.LENGTH_SHORT).show()

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
}
