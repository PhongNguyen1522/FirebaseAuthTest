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
import com.phongnn.firebaseloginsignup.data.User
import com.phongnn.firebaseloginsignup.databinding.FragmentHomeCommentBinding
import com.phongnn.firebaseloginsignup.ui.rcvadapter.UserAccountAdapter
import com.phongnn.firebaseloginsignup.ui.viewmodel.AuthViewModel
import com.phongnn.firebaseloginsignup.ui.viewmodel.SharedDataViewModel


class HomeCommentFragment : Fragment() {

    private lateinit var binding: FragmentHomeCommentBinding
    private lateinit var userViewModel: AuthViewModel
    private lateinit var sharedViewModel: SharedDataViewModel<User>

    private lateinit var userAccountAdapter: UserAccountAdapter

    private var userAccountList = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_comment, container, false)
        userViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedDataViewModel::class.java] as SharedDataViewModel<User>

        binding.authViewModel = userViewModel
        binding.lifecycleOwner = this

        initUI()
        observeUsersListFromDb()

        return binding.root
    }

    private fun initUI() {
        userAccountAdapter = UserAccountAdapter(requireContext(), userAccountList)
        userAccountAdapter.setOnItemClickListener { userAccount, position ->
            findNavController().navigate(R.id.action_homeCommentFragment_to_detailCommentFragment)
            sharedViewModel.sharedData = userAccount
        }

        binding.rcvMessagesList.apply {
            adapter = userAccountAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeUsersListFromDb() {
        userViewModel.apply {
            loadUsers()
            returnedUsers.observe(this@HomeCommentFragment) {
                it?.let {
                    userAccountList.clear()
                    userAccountList.addAll(it)

                    // Return email
                    val currentUserEmail = userViewModel.getCurrentUser()

                    for (i in 0 until userAccountList.size) {
                        if (userAccountList[i].email.equals(currentUserEmail, false)) {
                            userAccountList.removeAt(i)
                            break
                        }
                    }
                    // Update on Recycler View
                    userAccountAdapter.setUserAccountList(userAccountList)

                }
            }
        }


    }

}