package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raj.mygrowth.databinding.FragmentAndroidInterviewBinding
import com.raj.mygrowth.databinding.FragmentPasswordBinding
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch

class AndroidInterviewQuestionsFragment : Fragment() {

    private var _binding: FragmentAndroidInterviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAndroidInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDailyTasks()
    }

    private fun loadDailyTasks() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAndroidInterview("get_prep_link")

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    binding.rvPassword.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = AndroidInterviewQuestionAdapter(response.data)
                    binding.rvPassword.adapter = adapter
                    Toast.makeText(requireContext(), "Data Loaded", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // avoid memory leak
    }
}
