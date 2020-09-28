package dev.similin.cloudchat.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.similin.cloudchat.databinding.ContactListItemBinding
import timber.log.Timber

class ContactsRecyclerAdapter(val clickListener: (ContactsModel)->Unit) :
    RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {

    private val contactsList = mutableListOf<ContactsModel>()

    fun setList(listData: List<ContactsModel>) {
        contactsList.clear()
        contactsList.addAll(listData)
        Timber.e(contactsList.toString())
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ContactListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ContactListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.contacts = contactsList[position]
        holder.binding.contactLayout.setOnClickListener {
            clickListener(contactsList[position])
        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }
}