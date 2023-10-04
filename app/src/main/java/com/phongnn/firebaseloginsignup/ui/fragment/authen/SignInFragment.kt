package com.phongnn.firebaseloginsignup.ui.fragment.authen

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
import com.phongnn.firebaseloginsignup.databinding.FragmentSignInBinding
import com.phongnn.firebaseloginsignup.ui.viewmodel.AuthViewModel


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.authViewModel = authViewModel
        // For liveData to be observed
        binding.lifecycleOwner = viewLifecycleOwner

        authViewModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                binding.apply {
                    etEmail.text = null
                    etPassword.text = null
                }
            }
        }

        binding.btnGoToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        return binding.root
    }
}