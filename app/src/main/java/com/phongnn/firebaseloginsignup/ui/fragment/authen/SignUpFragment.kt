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
import com.phongnn.firebaseloginsignup.databinding.FragmentSignUpBinding
import com.phongnn.firebaseloginsignup.ui.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.authViewModel = authViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        authViewModel.signedUpInUser.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                binding.apply {
                    etEmail.text = null
                    etPassword.text = null
                }
            }
        }

        binding.btnGoToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        return binding.root
    }
}