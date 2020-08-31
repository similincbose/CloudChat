package dev.similin.cloudchat.ui.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.similin.cloudchat.CloudChatApplication
import dev.similin.cloudchat.databinding.FragmentContactsBinding


class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var factory: ContactsViewModelFactory
    private val viewModel by viewModels<ContactsViewModel>({ this }, { factory })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        val repository = (activity?.application as CloudChatApplication).getContactsRepository()
        factory = ContactsViewModelFactory(repository)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContacts()
    }

    private fun setupContactList() {
        val adapter = ContactsRecyclerAdapter()
        binding.rvContactList.adapter = adapter
        adapter.setList(viewModel.contacts)
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
        val builder = StringBuilder()
        val resolver: ContentResolver = requireContext().contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    )).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = requireContext().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )

                        if (cursorPhone != null) {
                            if (cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(
                                        cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                    builder.append("Contact: ").append(name)
                                        .append(", Phone Number: ").append(
                                            phoneNumValue
                                        ).append("\n\n")
                                    viewModel.contacts.add(
                                        ContactsModel(
                                            phoneNumValue, name
                                        )
                                    )
                                }
                                setupContactList()
                            }
                        }
                        cursorPhone?.close()
                    }
                }
            } else {
            }
        }
        cursor?.close()
    }


    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
}