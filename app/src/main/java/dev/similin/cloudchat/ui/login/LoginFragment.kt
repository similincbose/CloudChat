package dev.similin.cloudchat.ui.login

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hbb20.CountryCodePicker
import dev.similin.cloudchat.CloudChatApplication
import dev.similin.cloudchat.databinding.FragmentLoginBinding
import dev.similin.cloudchat.util.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), CountryCodePicker.OnCountryChangeListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var factory: LoginViewModelFactory
    private val viewModel by viewModels<LoginViewModel>({ this }, { factory })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val repository = (activity?.application as CloudChatApplication).getLoginRepository()
        factory = LoginViewModelFactory(repository)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.countryCodePicker.setDefaultCountryUsingNameCode("IN")
        setListenerMethods()
    }

    private fun setListenerMethods() {
        binding.countryCodePicker.setOnCountryChangeListener(this)
        binding.btnLogin.setOnClickListener {
            slideUp(binding.imageView2)
            slideDown(binding.pinview)
            binding.btnLogin.text = "Resend"
            binding.btnLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            viewModel.loginButtonClickFunction()
        }
    }

    override fun onCountrySelected() {
        viewModel.saveCountryCode(binding.countryCodePicker.selectedCountryCode)
    }



}