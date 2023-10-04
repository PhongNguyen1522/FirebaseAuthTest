package com.phongnn.firebaseloginsignup.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.phongnn.firebaseloginsignup.R
import com.phongnn.firebaseloginsignup.databinding.ActivityCommentBinding
import com.phongnn.firebaseloginsignup.ui.viewmodel.ChatViewModel

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment)

        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        binding.commentViewModel = chatViewModel

        chatViewModel.returnedComments.observe(this) {
            it?.let {
//                binding.tvReturnedComments.text = it.toString()
            }
        }


    }
}