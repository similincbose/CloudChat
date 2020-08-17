package dev.similin.cloudchat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker
import dev.similin.cloudchat.R
import dev.similin.cloudchat.databinding.FragmentLoginBinding


class LoginFragment : Fragment(), CountryCodePicker.OnCountryChangeListener {
    private lateinit var binding: FragmentLoginBinding;
    private var ccp: CountryCodePicker?=null
    private var countryCode:String?=null
    private var countryName:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(getLayoutInflater())
        binding.countryCodePicker!!.setOnCountryChangeListener(this)
        binding.countryCodePicker!!.setDefaultCountryUsingNameCode("IN")
        return binding.root
    }

    override fun onCountrySelected() {
        countryCode=binding.countryCodePicker!!.selectedCountryCode
        countryName=binding.countryCodePicker!!.selectedCountryName

        Toast.makeText(context,"Country Code "+countryCode,Toast.LENGTH_SHORT).show()
        Toast.makeText(context,"Country Name "+countryName,Toast.LENGTH_SHORT).show()
    }
}