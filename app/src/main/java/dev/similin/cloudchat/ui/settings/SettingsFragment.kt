package dev.similin.cloudchat.ui.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import dev.similin.cloudchat.R
import dev.similin.cloudchat.databinding.FragmentSettingsBinding
import dev.similin.cloudchat.ui.main.MainActivity


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as MainActivity).setToolbarTitle("Settings")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings_menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                showLogoutAlert()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutAlert() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure?")
            .setConfirmText("Logout")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
                auth.signOut()
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
            }
            .setCancelButton(
                "Cancel"
            ) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }
}