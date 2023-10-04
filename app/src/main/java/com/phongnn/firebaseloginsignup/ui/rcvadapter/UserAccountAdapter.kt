package com.phongnn.firebaseloginsignup.ui.rcvadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phongnn.firebaseloginsignup.R
import com.phongnn.firebaseloginsignup.data.User
import com.phongnn.firebaseloginsignup.databinding.ItemUsernameBinding

class UserAccountAdapter(
    private val context: Context,
    private var userAccountList: List<User>,
) : RecyclerView.Adapter<UserAccountAdapter.MyViewHolder>() {

    private var onItemClickListener: ((User, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemUsernameBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_username, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userAccountList.size
    }

    fun setUserAccountList(newUserList: List<User>) {
        this.userAccountList = newUserList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userAccount = userAccountList[position]
        holder.bind(userAccount, position)
        holder.itemView.setOnClickListener {
            onItemClick(userAccount, position)
        }
    }

    // Method to set the click listener from outside
    fun setOnItemClickListener(listener: (User, Int) -> Unit) {
        onItemClickListener = listener
    }

    // Callback method to handle item click
    private fun onItemClick(user: User, position: Int) {
        onItemClickListener?.invoke(user, position)

    }

    inner class MyViewHolder(private val binding: ItemUsernameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, position: Int) {

            if (user.avatarUrl == "null") {
                Glide.with(context)
                    .load(R.drawable.ic_avatar)
                    .into(binding.imvUserAvatar)
            } else {
                Glide.with(context)
                    .load(user.avatarUrl)
                    .into(binding.imvUserAvatar)
            }

            binding.tvUserName.text = user.userName
        }
    }

}