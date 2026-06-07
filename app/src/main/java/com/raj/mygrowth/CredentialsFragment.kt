package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.databinding.DialogBankDetailsUpdateBinding
import com.raj.mygrowth.databinding.DialogUpdatePasswordBinding
import com.raj.mygrowth.databinding.FragmentPasswordBinding
import com.raj.mygrowth.domain.BankItem
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.RequestActionUpdatePassword
import com.raj.mygrowth.domain.RequestDankDetailsUpdate
import com.raj.mygrowth.interfaces.AdapterClick
import com.raj.mygrowth.interfaces.BankClick
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import com.raj.mygrowth.repository.Repository
import com.raj.mygrowth.uiState.UiState
import com.raj.mygrowth.viewModel.CommonViewModel
import kotlinx.coroutines.launch

class CredentialsFragment : Fragment(), AdapterClick, BankClick {
    lateinit var dialogBottomSheetDialog: BottomSheetDialog
    lateinit var dialogBottomSheetDialogBankDetails: BottomSheetDialog
    private val viewModel: CommonViewModel by viewModels {
        CommonViewModel.CommonViewModelFactory(Repository(requireContext()))
    }

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        dialogBottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        dialogBottomSheetDialogBankDetails =
            BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
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
                    val adapter = PasswordAdapterItem(response.data, this@CredentialsFragment)
                    binding.rvPassword.adapter = adapter
                    //Toast.makeText(requireContext(), "Data Loaded", Toast.LENGTH_SHORT).show()
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
                    val adapter = BankDetailsAdapter(response.data, this@CredentialsFragment)
                    binding.rvPassword.adapter = adapter
                }


            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun dialog(id: String) {
        val binding = DialogUpdatePasswordBinding.inflate(layoutInflater)
        dialogBottomSheetDialog.setContentView(binding.root)

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
            val password: String = binding.editPassword.text.toString().trim()
            if (password.isNotEmpty()) {
                val requestAction = RequestActionUpdatePassword(
                    id = id,
                    password = password,
                    action = "get_update_password"
                )
                callApiUpdatePassword(requestAction)
            }
        }
    }

    fun dialogBankDetails(request: BankItem) {
        val binding = DialogBankDetailsUpdateBinding.inflate(layoutInflater)
        dialogBottomSheetDialogBankDetails.setContentView(binding.root)

        dialogBottomSheetDialogBankDetails.setOnShowListener {
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
        dialogBottomSheetDialogBankDetails.show()

        binding.apply {
            textBankName.text = request.bank_name
            editCustomerId.setText(request.customer_id)
            editAccountNumber.setText(request.account_number)
            editCardNumber.setText(request.card_number)
            editPin.setText(request.card_pin)
            editLoginUserName.setText(request.user_name)
            editLoginPassword.setText(request.login_password)
            editProfilePassword.setText(request.profile_password)
            editIFSC.setText(request.ifsc_code)
        }
        binding.btnSubmit.setOnClickListener {
            val password: String = binding.editPin.text.toString().trim()

            if (password.isNotEmpty()) {
                val requestAction = RequestDankDetailsUpdate(
                    action = "update_bank_details",
                    id = request.id,
                    name = request.bank_name,
                    cust_id = getTextValue(binding.editCustomerId),
                    acc_number = getTextValue(binding.editAccountNumber),
                    card_num = getTextValue(binding.editCardNumber),
                    cpin = getTextValue(binding.editPin),
                    psw = getTextValue(binding.editLoginPassword),
                    profile_password = getTextValue(binding.editProfilePassword),
                    ifsc_code = getTextValue(binding.editIFSC),
                    user_name = getTextValue(binding.editLoginUserName)
                )
                callApiUpdateBankDetails(requestAction)
            }
        }
    }


    private fun callApiUpdatePassword(request: RequestActionUpdatePassword) {
        viewModel.fetchUpdatePassword(request)
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessCommon -> {
                        dialogBottomSheetDialog.dismiss()
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {}
                }
            }
        }
    }

    private fun callApiUpdateBankDetails(request: RequestDankDetailsUpdate) {
        viewModel.fetchUpdateBankDetails(request)
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        // show loader
                    }

                    is UiState.SuccessCommon -> {
                        dialogBottomSheetDialogBankDetails.dismiss()
                    }

                    is UiState.Error -> {
                        // show error
                    }

                    else -> {}
                }
            }
        }
    }

    override fun click(id: String) {
        dialog(id)
    }

    override fun clickBankDetails(request: BankItem) {
        dialogBankDetails(request)
    }

    fun getTextValue(edittext: EditText): String {
        val str = edittext.text.toString().trim()
        return str
    }
}
