package com.phongnn.firebaseloginsignup.ui.fragment.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phongnn.firebaseloginsignup.R
import com.phongnn.firebaseloginsignup.data.Comment
import com.phongnn.firebaseloginsignup.data.User
import com.phongnn.firebaseloginsignup.databinding.FragmentHomeCommentBinding
import com.phongnn.firebaseloginsignup.ui.rcvadapter.MessagesHomeAdapter
import com.phongnn.firebaseloginsignup.ui.rcvadapter.UserAccountAdapter
import com.phongnn.firebaseloginsignup.ui.viewmodel.AuthViewModel
import com.phongnn.firebaseloginsignup.ui.viewmodel.ChatViewModel
import com.phongnn.firebaseloginsignup.ui.viewmodel.SharedDataViewModel


class HomeCommentFragment : Fragment() {

    private lateinit var binding: FragmentHomeCommentBinding
    private lateinit var userViewModel: AuthViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var sharedViewModel: SharedDataViewModel<User>

    private lateinit var userAccountAdapter: UserAccountAdapter
    private lateinit var messageHomeAdapter: MessagesHomeAdapter

    private var userAccountList = mutableListOf<User>()
    private var allMessagesList = mutableListOf<Comment>()
    private var currentEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_comment, container, false)
        userViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        chatViewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
        sharedViewModel =
            ViewModelProvider(requireActivity())[SharedDataViewModel::class.java] as SharedDataViewModel<User>

        binding.authViewModel = userViewModel
        binding.lifecycleOwner = this
        currentEmail = userViewModel.getCurrentUser()

        initUI()
        observeUsersListFromDb()

        return binding.root
    }

    private fun initUI() {

        // User Account Adapter
        userAccountAdapter = UserAccountAdapter(requireContext(), userAccountList)
        userAccountAdapter.setOnItemClickListener { userAccount, position ->
            findNavController().navigate(R.id.action_homeCommentFragment_to_detailCommentFragment)
            sharedViewModel.sharedData = userAccount
        }

        binding.rcvUserNameList.apply {
            adapter = userAccountAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        messageHomeAdapter = MessagesHomeAdapter(
            requireContext(),
            userAccountList,
            userViewModel.getCurrentUser(),
            allMessagesList
        )

        binding.rcvMessagesList.apply {
            adapter = messageHomeAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        // On click
        binding.btnLogout.setOnClickListener {
            findNavController().navigate(R.id.action_homeCommentFragment_to_homeFragment2)
        }

    }

    private fun observeUsersListFromDb() {
        userViewModel.apply {
            loadUsers()
            returnedUsers.observe(this@HomeCommentFragment) {
                it?.let {
                    userAccountList.clear()
                    userAccountList.addAll(it)


                    for (i in 0 until userAccountList.size) {
                        if (userAccountList[i].email.equals(currentEmail, false)) {
                            userAccountList.removeAt(i)
                            break
                        }
                    }
                    // Update on Recycler View
                    userAccountAdapter.setUserAccountList(userAccountList)

                }
            }
        }

        // Chat View Model
        chatViewModel.apply {
            getAllMessagesList(currentEmail)
            returnedComments.observe(this@HomeCommentFragment) {
                it?.let {
                    allMessagesList.clear()
                    allMessagesList.addAll(it)
                }
                // Update Rcv
                messageHomeAdapter.setMessageList(allMessagesList)
            }
        }

    }

}