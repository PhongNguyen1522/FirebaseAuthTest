package com.phongnn.firebaseloginsignup.ui.rcvadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.phongnn.firebaseloginsignup.R
import com.phongnn.firebaseloginsignup.data.Comment
import com.phongnn.firebaseloginsignup.databinding.ItemMyAllMessagesBinding
import com.phongnn.firebaseloginsignup.databinding.ItemYourAllMessagesBinding

class DetailedCommentAdapter(
    private val context: Context,
    private var messagesList: List<Comment>,
    private val senderEmail: String,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_MY_MESSAGE = 1
        const val VIEW_TYPE_OTHER_MESSAGE = 2
    }

    inner class MyCommentViewHolder(private val binding: ItemMyAllMessagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Comment, position: Int) {
            binding.tvMessageContent.text = message.text
            binding.tvTime.text = message.time
        }
    }

    inner class YourCommentViewHolder(private val binding: ItemYourAllMessagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Comment, position: Int) {
            binding.tvMessageContent.text = message.text
            binding.tvTime.text = message.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                val binding = DataBindingUtil.inflate<ItemMyAllMessagesBinding>(
                    layoutInflater,
                    R.layout.item_my_all_messages,
                    parent,
                    false
                )
                MyCommentViewHolder(binding)
            }

            VIEW_TYPE_OTHER_MESSAGE -> {
                val binding = DataBindingUtil.inflate<ItemYourAllMessagesBinding>(
                    layoutInflater,
                    R.layout.item_your_all_messages,
                    parent,
                    false
                )
                YourCommentViewHolder(binding)
            }
            else -> {
                throw java.lang.IllegalArgumentException("Invalid View Type")
            }
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messagesList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                (holder as MyCommentViewHolder).bind(message, position)
            }
            VIEW_TYPE_OTHER_MESSAGE -> {
                (holder as YourCommentViewHolder).bind(message, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messagesList[position]
        return if (message.senderEmail.equals(
                senderEmail,
                false
            )
        ) VIEW_TYPE_MY_MESSAGE else VIEW_TYPE_OTHER_MESSAGE
    }

    fun setMessageList(messageList: List<Comment>) {
        this.messagesList = messageList
        notifyDataSetChanged()
    }

}