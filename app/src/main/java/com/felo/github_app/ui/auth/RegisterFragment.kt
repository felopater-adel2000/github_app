package com.felo.github_app.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.felo.github_app.R
import com.felo.github_app.base.BaseFragment
import com.felo.github_app.databinding.FragmentRegisterBinding
import com.felo.github_app.network.DataState
import com.felo.github_app.utils.DataStateChangeListener
import com.felo.github_app.utils.displaySuccessDialog
import com.felo.github_app.utils.hide
import com.felo.github_app.utils.hideSoftKeyboard
import com.felo.github_app.utils.show
import com.felo.github_app.viewModel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class RegisterFragment : BaseFragment(), View.OnClickListener {
    private val TAG = "RegisterFragment"
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by activityViewModel()
    private var stateChangeListener: DataStateChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        initViews()
        collectDataValidation()
        return binding.root
    }

    private fun initViews()
    {
        binding.apply {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    checkDataValidation()
                }
            }
            etEmail.addTextChangedListener(textWatcher)
            etPassword.addTextChangedListener(textWatcher)
            etConfirmPassword.addTextChangedListener(textWatcher)
            btnRegister.setOnClickListener(this@RegisterFragment)
            tvBackToLogin.setOnClickListener(this@RegisterFragment)
        }
    }

    private fun checkDataValidation() {
        viewModel.checkRegisterFormValidation(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString().trim(),
            confirmationPassword = binding.etConfirmPassword.text.toString().trim(),
        )
    }

    private fun collectDataValidation() = lifecycleScope.launch{
        viewModel.registerFormState.collect {formState ->
            binding.btnRegister.isEnabled = formState.isDataValid

            resetRegisterFormErrors()
            if(!formState.isEmailValid) binding.tvEmailError.show()
            else if(!formState.isPasswordValid) binding.tvPasswordError.show()
            else if(!formState.isConfirmationPasswordValid) binding.tvConfirmPasswordError.show()
        }
    }

    private fun resetRegisterFormErrors() {
        binding.apply {

            tvEmailError.hide()

            tvPasswordError.hide()

            tvConfirmPasswordError.hide()
        }
    }

    private fun register() = lifecycleScope.launch {
        requireActivity().hideSoftKeyboard()
        viewModel.register(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString().trim(),
        ).collect {response ->
            showProgressBar(response.loading.isLoading)

            if(response is DataState.Error)
            {
                response.error?.let { stateChangeListener?.onErrorStateChange(it) }
            }
            else if(response is DataState.Success)
            {
                Log.d(TAG, "register: Success Login")
                Firebase.auth.signOut()
                requireActivity().displaySuccessDialog(R.string.success) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun showProgressBar(show: Boolean) {
        binding.apply {
            if(show)
            {
                btnRegister.hide()
                pbLoading.show()
            }
            else
            {
                pbLoading.hide()
                btnRegister.show()
            }
        }
    }

    override fun showErrorUI(
        show: Boolean,
        image: Int?,
        title: String?,
        desc: String?,
        isAuthenticated: Boolean?,
        showButton: Boolean?
    ) {

    }

    override fun onClick(v: View?) {
        binding.apply {
            when(v) {
                btnRegister -> register()

                tvBackToLogin -> findNavController().popBackStack()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

}