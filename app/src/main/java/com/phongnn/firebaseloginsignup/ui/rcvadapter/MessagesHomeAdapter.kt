package com.phongnn.firebaseloginsignup.ui.rcvadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phongnn.firebaseloginsignup.R
import com.phongnn.firebaseloginsignup.data.Comment
import com.phongnn.firebaseloginsignup.data.User
import com.phongnn.firebaseloginsignup.databinding.ItemEachMessageBinding

class MessagesHomeAdapter(
    val context: Context,
    private val accountList: List<User>,
    private val currentUserEmail: String,
    private var allMessagesList: List<Comment>,
) : RecyclerView.Adapter<MessagesHomeAdapter.MessageHomeViewHolder>() {


    inner class MessageHomeViewHolder(
        private val binding: ItemEachMessageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Comment, position: Int) {

            var contactEmail = if (currentUserEmail.equals(message.senderEmail, false)) {
                message.receiverEmail
            } else {
                message.senderEmail
            }

            for (acc in accountList) {
                if (contactEmail.equals(acc.email, false)) {
                    Glide.with(context)
                        .load(acc.avatarUrl)
                        .into(binding.imvUserAvatar)
                    binding.tvUserName.text = acc.userName
                    binding.tvUserMessage.text = message.text
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemEachMessageBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_each_message, parent, false)
        return MessageHomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allMessagesList.size
    }

    override fun onBindViewHolder(holder: MessageHomeViewHolder, position: Int) {
        val message = allMessagesList[position]
        holder.bind(message, position)
    }

    fun setMessageList(list: List<Comment>) {
        this.allMessagesList = list
        notifyDataSetChanged()
    }

}