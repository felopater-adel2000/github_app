package com.felo.github_app.ui.auth

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.felo.github_app.R
import com.felo.github_app.base.BaseFragment
import com.felo.github_app.databinding.FragmentLoginBinding
import com.felo.github_app.network.DataState
import com.felo.github_app.ui.home.MainActivity
import com.felo.github_app.utils.DataStateChangeListener
import com.felo.github_app.utils.displayToast
import com.felo.github_app.utils.hide
import com.felo.github_app.utils.hideSoftKeyboard
import com.felo.github_app.utils.show
import com.felo.github_app.viewModel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class LoginFragment : BaseFragment(), View.OnClickListener {
    private val TAG = "LoginFragment"
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by activityViewModel()
    private var stateChangeListener: DataStateChangeListener? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private val callbackManager = CallbackManager.Factory.create()

    private val googleResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if(result.resultCode == RESULT_OK)
        {
            Log.d(TAG, "REsult OK: ")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            if(task.isSuccessful && task.result != null)
            {
                val credential = GoogleAuthProvider.getCredential(task.result.idToken, null)
                Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        onLoginSuccess()
                    }
                    else
                    {
                        displayToast("Google Sign In Failed")
                    }
                }
            }
            else
            {
                displayToast("Google Sign In Failed")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        initViews()
        collectLoginDataValidation()
        initGoogleSettings()
        initFacebookSettings()
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

            btnLogin.setOnClickListener(this@LoginFragment)
            tvRegisterNow.setOnClickListener(this@LoginFragment)
            tvLoginWithGoogle.setOnClickListener(this@LoginFragment)
            tvLoginWithFacebook.setOnClickListener(this@LoginFragment)
        }
    }

    private fun loginWithEmailAndPassword() = lifecycleScope.launch {
        requireActivity().hideSoftKeyboard()
        viewModel.login(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString()
        ).collect {response ->
            showProgressBar(response.loading.isLoading)

            if(response is DataState.Error)
            {
                response.error?.let { stateChangeListener?.onErrorStateChange(it) }
            }
            else if(response is DataState.Success)
            {
                onLoginSuccess()
            }
        }
    }

    private fun collectLoginDataValidation() = lifecycleScope.launch {
        viewModel.loginFormState.collect {formState ->
            binding.btnLogin.isEnabled = formState.isDataValid

            resetLoginFormErrors()
            if(!formState.isEmailValid) binding.tvEmailError.show()
            if(!formState.isPasswordValid) binding.tvPasswordError.show()
        }
    }

    private fun resetLoginFormErrors()
    {
        binding.apply {
            tvEmailError.hide()
            tvPasswordError.hide()
        }
    }

    private  fun checkDataValidation()
    {
        viewModel.checkLoginFormValidation(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
    }

    private fun initGoogleSettings()
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initFacebookSettings()
    {
        binding.facebookLoginButton.setPermissions("email")
        binding.facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                displayToast("Facebook Sign In Cancelled")
            }

            override fun onError(error: FacebookException) {
                displayToast("Facebook Sign In Failed")
            }

            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "onSuccess: ${result.accessToken.token}")
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        onLoginSuccess()
                    }
                    else
                    {
                        displayToast("Facebook Sign In Failed")
                    }
                }
            }

        })
    }

    private fun loginWithGoogle()
    {
        val signInIntent = googleSignInClient.signInIntent
        googleResultLauncher.launch(signInIntent)
    }

    private fun loginWithFacebook()
    {
        binding.facebookLoginButton.performClick()
    }

    private fun onLoginSuccess()
    {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun showProgressBar(show: Boolean) {
        binding.apply {
            if(show)
            {
                pbLoading.show()
                btnLogin.hide()
            }
            else
            {
                pbLoading.hide()
                btnLogin.show()
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
                btnLogin -> loginWithEmailAndPassword()

                tvRegisterNow -> findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

                tvLoginWithGoogle -> loginWithGoogle()

                tvLoginWithFacebook -> loginWithFacebook()
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