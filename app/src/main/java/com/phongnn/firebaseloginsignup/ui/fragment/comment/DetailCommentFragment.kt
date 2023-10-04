package com.phongnn.firebaseloginsignup.ui.fragment.comment

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.phongnn.firebaseloginsignup.R
import com.phongnn.firebaseloginsignup.data.Comment
import com.phongnn.firebaseloginsignup.data.User
import com.phongnn.firebaseloginsignup.databinding.FragmentDetailCommentBinding
import com.phongnn.firebaseloginsignup.ui.rcvadapter.DetailedCommentAdapter
import com.phongnn.firebaseloginsignup.ui.viewmodel.AuthViewModel
import com.phongnn.firebaseloginsignup.ui.viewmodel.ChatViewModel
import com.phongnn.firebaseloginsignup.ui.viewmodel.SharedDataViewModel

class DetailCommentFragment : Fragment() {

    private lateinit var binding: FragmentDetailCommentBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var sharedViewModel: SharedDataViewModel<User>
    private lateinit var detailedCommentAdapter: DetailedCommentAdapter

    private var messagesList = mutableListOf<Comment>()

    private lateinit var sendEmail: String
    private lateinit var receiveEmail: String
    private lateinit var receiveUser: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_comment, container, false)
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        sharedViewModel =
            ViewModelProvider(requireActivity())[SharedDataViewModel::class.java] as SharedDataViewModel<User>

        binding.commentViewModel = chatViewModel
        binding.lifecycleOwner = this

        sendEmail = authViewModel.getCurrentUser()
        sharedViewModel.sharedData?.let { user ->
            receiveUser = user
            user.email?.let { email ->
                receiveEmail = email
            }
        }

        initUi()
        observeMessageList()

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    private fun observeMessageList() {
        chatViewModel.loadMessages(sendEmail, receiveEmail)
        chatViewModel.returnedComments.observe(requireActivity()) {
            it?.let {
                messagesList.clear()
                messagesList.addAll(it)
                detailedCommentAdapter.setMessageList(messagesList)
            }
        }
    }

    private fun initUi() {

        Glide.with(requireActivity())
            .load(receiveUser.avatarUrl)
            .into(binding.imvUserAvatar)

        binding.tvUserName.text = receiveUser.userName

        binding.btnSendComment.setOnClickListener {
            chatViewModel.sendMessage(sendEmail, receiveEmail)
            binding.edtComment.setText("")
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailCommentFragment_to_homeCommentFragment)
        }

        // Recycler View
        val currentEmail = authViewModel.getCurrentUser()
        detailedCommentAdapter =
            DetailedCommentAdapter(requireContext(), messagesList, currentEmail)
        binding.rcvMessages.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = detailedCommentAdapter
        }
    }

}