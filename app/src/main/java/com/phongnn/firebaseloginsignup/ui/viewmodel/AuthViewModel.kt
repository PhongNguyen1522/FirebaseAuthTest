package com.phongnn.firebaseloginsignup.ui.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.phongnn.firebaseloginsignup.data.User

private const val MY_TAG = "PHONGNN4"

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val usersList = mutableListOf<User>()

    val emailLiveData = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String?>()

    private var _loggedInUser = MutableLiveData<User?>()
    val loggedInUser: LiveData<User?> = _loggedInUser

    private var _signedUpInUser = MutableLiveData<User?>()
    val signedUpInUser: LiveData<User?> = _signedUpInUser

    init {
        usersList.add(User("phongnn1@gmail.com", "123456"))
        usersList.add(User("phongnn2@gmail.com", "123456"))
    }

    fun getCurrentUser(): String {
        val user = firebaseAuth.currentUser
        return user?.email.toString()
    }

    fun signUp() {
        val user = checkInputForm()
        if (user != null) {
            firebaseAuth.createUserWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signedUpInUser.value = user
                        showToast("Sign-up Success!")
                    } else {
                        _signedUpInUser.value = null
                        showToast("Sign-up Failed!")
                        Log.e(MY_TAG, task.exception?.message.toString())
                    }
                }
        } else {
            _signedUpInUser.value = null
            showToast("Sign-up Failed!")
        }
    }

    fun signIn() {
        val user = checkInputForm()
        if (user != null) {
            firebaseAuth.signInWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loggedInUser.value = user
                        showToast("Sign-in Success")
                    }else {
                        _loggedInUser.value = null
                        showToast("Sign-in Failed")
                        Log.e(MY_TAG, task.exception?.message.toString())
                    }
                }
        } else {
            _loggedInUser.value = null
            showToast("Sign-in Failed!")
        }
    }

    fun signOut() {
        _loggedInUser.value = null
        showToast("Log-out Success!")
    }

    private fun showToast(message: String) {
        val context = getApplication<Application>().applicationContext
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkInputForm(): User? {
        val email = emailLiveData.value
        var isValidEmail = isEmailValid(email)

        val password = passwordLiveData.value
        var isValidPassword = isPasswordValid(password)

        if (!isValidEmail || !isValidPassword) {
            if (!isValidEmail) {
                showToast("Invalid Email!")
            }
            if (!isValidPassword) {
                showToast("Invalid Password!")
            }
            return null
        }

        return User(email, password)
    }

    private fun isEmailValid(email: String?): Boolean {
        if (email != null) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return true
            }
        }
        return false
    }

    private fun isPasswordValid(password: String?): Boolean {
        val digitPattern = ".*\\d.*" // Check for at least one digit
        val specialCharPattern =
            ".*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*" // Check for at least one special character
        val minLength = 6 // Minimum password length

        if (password != null) {
            if ((password.length >= minLength))
                return true
        }
//        return (password.length >= minLength &&
//                password.matches(digitPattern.toRegex()) &&
//                password.matches(specialCharPattern.toRegex()))
        return false
    }


}