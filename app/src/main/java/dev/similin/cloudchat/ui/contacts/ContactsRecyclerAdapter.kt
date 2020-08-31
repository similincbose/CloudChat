package dev.similin.cloudchat.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.similin.cloudchat.databinding.ContactListItemBinding

class ContactsRecyclerAdapter : RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {

    private val contactsList = mutableListOf<ContactsModel>()

    fun setList(listData: List<ContactsModel>) {
        contactsList.clear()
        contactsList.addAll(listData)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding:ContactListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactsRecyclerAdapter.ViewHolder {
        return ViewHolder(
            ContactListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactsRecyclerAdapter.ViewHolder, position: Int) {
        holder.binding.contacts = contactsList[position]
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }
}