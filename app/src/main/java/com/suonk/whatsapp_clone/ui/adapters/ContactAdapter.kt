package com.suonk.whatsapp_clone.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.ItemMessageBinding
import com.suonk.whatsapp_clone.models.Contact
import com.suonk.whatsapp_clone.ui.activity.MainActivity

class ContactAdapter(private val activity: MainActivity) :
    ListAdapter<Contact, ContactAdapter.ViewHolder>(ContactComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.onBind(contact)
    }

    inner class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(contact: Contact) {
            binding.apply {
                itemMessageContactName.text = contact.contactName

                Glide.with(activity)
                    .load(contact.contactImg)
                    .centerCrop()
                    .into(itemMessageContactImg)
            }
        }
    }

    class ContactComparator : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.contactName == newItem.contactName &&
                    oldItem.contactImg == newItem.contactImg &&
                    oldItem.phoneNumber == newItem.phoneNumber
        }
    }
}