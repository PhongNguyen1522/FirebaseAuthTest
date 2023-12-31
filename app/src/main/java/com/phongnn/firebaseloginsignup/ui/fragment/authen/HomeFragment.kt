package com.phongnn.firebaseloginsignup.ui.fragment.authen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.phongnn.firebaseloginsignup.R
import com.phongnn.firebaseloginsignup.databinding.FragmentHomeBinding
import com.phongnn.firebaseloginsignup.ui.activity.CommentActivity
import com.phongnn.firebaseloginsignup.ui.viewmodel.AuthViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.authViewModel = authViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // When sign-out button is clicked, user is null
        authViewModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
            }
        }

        // Go to comment Activity
        binding.btnOpenComment.setOnClickListener {
            val intent = Intent(activity, CommentActivity::class.java)
            startActivity(intent)
        }

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        return binding.root
    }

}