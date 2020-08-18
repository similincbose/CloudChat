package dev.similin.cloudchat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dev.similin.cloudchat.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>({ this })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.countryCodePicker.setDefaultCountryUsingNameCode("IN")
        viewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, "{$it}", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.countryCode.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, "{$it}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}