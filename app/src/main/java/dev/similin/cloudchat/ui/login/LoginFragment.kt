package dev.similin.cloudchat.ui.login

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showDrawable
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.similin.cloudchat.R
import dev.similin.cloudchat.databinding.FragmentLoginBinding
import dev.similin.cloudchat.network.Status
import dev.similin.cloudchat.util.slideDown
import dev.similin.cloudchat.util.slideUp
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : Fragment(), CountryCodePicker.OnCountryChangeListener {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>({ this })
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        bindProgressButton(binding.btnLogin)
        bindProgressButton(binding.btnVerify)
        auth = FirebaseAuth.getInstance()
        viewModel.uid = auth.currentUser?.uid
        setFirebaseCallBack()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.countryCodePicker.setDefaultCountryUsingNameCode("IN")
        viewModel.saveCountryCode("91")
        binding.edtPhoneNumber.requestFocus()
        setListenerMethods()
        getUsers();
    }


    private fun setListenerMethods() {
        binding.countryCodePicker.setOnCountryChangeListener(this)
        binding.btnLogin.setOnClickListener {
            binding.btnLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            binding.btnLogin.showProgress {
                progressColor = Color.WHITE
            }
            startPhoneNumberVerification()
        }

        binding.btnVerify.setOnClickListener {
            val code = binding.pinview.value
            binding.btnVerify.requestFocus()
            binding.btnVerify.showProgress {
                progressColor = Color.WHITE

            }
            code?.let { pin ->
                if (pin.length == 6) {
                    verifyPhoneNumberWithCode(viewModel.storedVerificationId, pin)
                } else {
                    Toast.makeText(context, "Invalid Code", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.tvResend.setOnClickListener {
            slideDown(it)
            viewModel.phoneNumber?.let {
                resendVerificationCode()
            }
        }
    }

    override fun onCountrySelected() {
        viewModel.saveCountryCode(binding.countryCodePicker.selectedCountryCode)
    }

    override fun onResume() {
        super.onResume()
        viewModel.clicked.observe(viewLifecycleOwner, {
            it?.let { clicked ->
                if (clicked) {
                    updateUI()
                }
            }
        })
    }

    private fun updateUI() {
        slideUp(binding.logo)
        slideDown(binding.pinview)
        binding.btnLogin.visibility = View.GONE
        slideDown(binding.btnVerify)
        slideDown(binding.tvResendOtp)
        slideDown(binding.tvTimerResend)
        binding.edtPhoneNumber.isEnabled = false
        binding.btnLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    private fun setFirebaseCallBack() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                viewModel.verificationInProgress = false
            }

            override fun onVerificationFailed(e: FirebaseException) {
                viewModel.verificationInProgress = false
                changeButton(R.drawable.ic_next, R.string.cntnue, binding.btnLogin)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(context, "Quote exceeded", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                viewModel.onLoginButtonClicked()
                updateUI()
                viewModel.storedVerificationId = verificationId
                viewModel.startTimer(TIME_IN_MILLI_SECONDS)
                updateTimer()
                viewModel.resendingToken = token
            }
        }
    }

    private fun updateTimer() {
        viewModel.currentTime.observe(viewLifecycleOwner, {
            it?.let { time ->
                when (time) {
                    "00:00" -> {
                        binding.tvResendOtp.visibility = View.GONE
                        binding.tvTimerResend.visibility = View.GONE
                        slideUp(binding.tvResend)
                    }
                    else -> binding.tvTimerResend.text = time
                }
            }
        })
    }

    private fun startPhoneNumberVerification() {
        viewModel.phoneNumber?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+${viewModel.getCountryCode()}${it}",
                60,
                TimeUnit.SECONDS,
                requireActivity(),
                callbacks
            )
        }
        viewModel.verificationInProgress = true
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        viewModel.saveUserID(user.uid)
                        viewModel.phoneNumber?.let { viewModel.saveUserPhoneNumber(it) }
                        changeButton(R.drawable.ic_tick, R.string.verified, binding.btnVerify)
                        getUsers()
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    binding.btnVerify.hideProgress("Verify")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }

    private fun resendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+${viewModel.getCountryCode()}${viewModel.phoneNumber}",
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            callbacks,
            viewModel.resendingToken
        )
    }

    private fun getUsers() {
        viewModel.fetchUsers().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.Success -> {
                        resource.data?.body()?.let { data ->
                            viewModel.checkUser(data)
                        }
                        when (viewModel.found) {
                            true -> welcomebackMsg()
                            else -> {
                                viewModel.writeNewUser()
                                loginSuccess()
                            }
                        }
                    }
                    Status.Loading -> {
                        Timber.d("Loading")
                    }
                    Status.Error -> {
                        Toast.makeText(
                            context,
                            "There was an error fetching data. Please check your internet connection. App needs to be run once to cache data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

    }

    private fun loginSuccess() {
        if (viewModel.flag!!) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
    }

    private fun welcomebackMsg() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("Welcome Back")
            .setConfirmText("Continue")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
                viewModel.flag = true
                loginSuccess()
            }
            .show()
    }

    private fun changeButton(drawable: Int, msg: Int, btn: MaterialButton) {
        val animatedDrawable =
            ContextCompat.getDrawable(requireContext(), drawable)
        animatedDrawable?.setBounds(0, 0, 40, 40)
        if (animatedDrawable != null) {
            btn.showDrawable(animatedDrawable) {
                buttonTextRes = msg
            }
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val TIME_IN_MILLI_SECONDS = 60000L
    }
}

