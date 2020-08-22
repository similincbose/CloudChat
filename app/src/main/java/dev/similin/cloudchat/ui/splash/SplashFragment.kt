package dev.similin.cloudchat.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dev.similin.cloudchat.CloudChatApplication
import dev.similin.cloudchat.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var factory: SplashViewModelFactory
    private val viewModel by viewModels<SplashViewModel>({ this }, { factory })
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        val repository = (activity?.application as CloudChatApplication).getSplashRepository()
        factory = SplashViewModelFactory(repository)
        auth = FirebaseAuth.getInstance()
        viewModel.uid = auth.currentUser?.uid
        lifecycleScope.launch {
            delay(3000)
            checkLoginUser()

        }

        return binding.root
    }

    private fun checkLoginUser() {
        if (viewModel.uid.equals(viewModel.getUserID())) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }

}