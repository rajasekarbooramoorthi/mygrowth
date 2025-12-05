package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raj.mygrowth.databinding.FragmentPasswordBinding
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch

class CredentialsFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPassword()
        click()
    }

    private fun loadPassword() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getPassword(RequestAction("get_master_password"))

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    binding.rvPassword.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = PasswordAdapterItem(response.data)
                    binding.rvPassword.adapter = adapter
                    Toast.makeText(requireContext(), "Data Loaded", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun click() {
        binding.cardPassword.setOnClickListener {
            loadPassword()
        }
        binding.cardBanks.setOnClickListener {
            loadBank()
        }
        binding.cardOthers.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // avoid memory leak
    }

    private fun loadBank() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getBankDetails(RequestAction("get_master_Bank"))

                binding.progressBar.visibility = View.GONE

                if (response.status) {
                    binding.rvPassword.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = BankDetailsAdapter(response.data)
                    binding.rvPassword.adapter = adapter
                    Toast.makeText(requireContext(), "Data Loaded", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
