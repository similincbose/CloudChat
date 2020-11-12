package dev.similin.cloudchat.ui.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.similin.cloudchat.databinding.FragmentContactsBinding
import dev.similin.cloudchat.network.Status
import dev.similin.cloudchat.ui.main.MainActivity
import timber.log.Timber

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private val viewModel by viewModels<ContactsViewModel>({ this })
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        auth = FirebaseAuth.getInstance()
        (activity as MainActivity).supportActionBar?.title = "Select Contact"
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContacts()
    }

    private fun setupRecyclerView() {
        val adapter = ContactsRecyclerAdapter {
            findNavController().navigate(
                ContactsFragmentDirections.actionContactsFragmentToChatFragment(
                    it.contactNumber?.replace(" ", "").toString(),
                    it.contactName.toString()
                )
            )
        }
        binding.rvContactList.adapter = adapter
        viewModel.contactList.observe(viewLifecycleOwner) {
            val orderedContactList = it.distinct()
            adapter.setList(orderedContactList)
        }
    }


    private fun loadContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                requireContext(), Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            getContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                Toast.makeText(
                    context,
                    "Permission must be granted in order to display contacts information",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getContacts() {

        val projection = arrayOf(
            Phone._ID,
            Phone.DISPLAY_NAME,
            Phone.NUMBER
        )

        val cr: ContentResolver = requireContext().contentResolver

        cr.query(
            Phone.CONTENT_URI,
            projection,
            null,
            null,
            Phone.DISPLAY_NAME + " ASC"
        )?.use { cursor ->
            val contactIdIndex =
                cursor.getColumnIndex(Phone._ID)
            val displayNameIndex =
                cursor.getColumnIndex(Phone.DISPLAY_NAME)
            val phoneNumber =
                cursor.getColumnIndex(Phone.NUMBER)
            var contactId: Long? = null
            var displayName: String
            var address: String
            while (cursor.moveToNext()) {
                contactId = cursor.getLong(contactIdIndex)
                displayName = cursor.getString(displayNameIndex)
                address = cursor.getString(phoneNumber)
                viewModel.contacts.add(
                    ContactsModel(
                        address, displayName
                    )
                )
            }
            getUsers()
        }
    }

    private fun getUsers() {
        viewModel.fetchUsers().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.Success -> {

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

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
}