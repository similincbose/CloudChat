package dev.similin.cloudchat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import dev.similin.cloudchat.CloudChatApplication
import dev.similin.cloudchat.databinding.FragmentLoginBinding
import dev.similin.cloudchat.util.slideDown
import dev.similin.cloudchat.util.slideUp
import java.util.concurrent.TimeUnit


class LoginFragment : Fragment(), CountryCodePicker.OnCountryChangeListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var factory: LoginViewModelFactory
    private val viewModel by viewModels<LoginViewModel>({ this }, { factory })
    private lateinit var auth: FirebaseAuth
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val repository = (activity?.application as CloudChatApplication).getLoginRepository()
        factory = LoginViewModelFactory(repository)
        binding.viewModel = viewModel
        auth = FirebaseAuth.getInstance()
        setFirebaseCallBack();
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.countryCodePicker.setDefaultCountryUsingNameCode("IN")
        viewModel.saveCountryCode("91")
        binding.edtPhoneNumber.requestFocus()
        setListenerMethods()
    }


    private fun setListenerMethods() {
        binding.countryCodePicker.setOnCountryChangeListener(this)
        binding.btnLogin.setOnClickListener {
            startPhoneNumberVerification()
        }

        binding.btnVerify.setOnClickListener {
            val code = binding.pinview.value
            binding.btnVerify.requestFocus()
            code?.let { pin ->
                if (pin.length == 6) {
                    verifyPhoneNumberWithCode(viewModel.storedVerificationId, pin)
                } else {
                    Toast.makeText(context, "Invalid Code", Toast.LENGTH_SHORT).show()
                }
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
                    doAnimation()
                }
            }
        })
    }

    private fun doAnimation() {
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
                doAnimation()
                viewModel.storedVerificationId = verificationId
                viewModel.startTimer(TIME_IN_MILLI_SECONDS)
                updateTimer();
                resendToken = token
            }
        }
    }

    private fun updateTimer() {
        viewModel.currentTime.observe(viewLifecycleOwner, Observer {
            it?.let { time ->
                when (time) {
                    "00:00" -> {
                        binding.tvResendOtp.visibility = View.GONE
                        binding.tvTimerResend.visibility = View.GONE
                    }
                    else -> binding.tvTimerResend.text = time
                }

            }
        })
    }

    private fun startPhoneNumberVerification() {
        viewModel.phoneNumber?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+" + viewModel.getCountryCode() + "" + it,
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
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val TIME_IN_MILLI_SECONDS = 60000L
    }
}

