package com.phongnn.firebaseloginsignup.ui.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.phongnn.firebaseloginsignup.data.User

private const val MY_TAG = "PHONGNN4"

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")

    // Create a live data to observe all users list
    private var _returnedUsers = MutableLiveData<List<User>?>()
    val returnedUsers: LiveData<List<User>?> = _returnedUsers


    val emailLiveData = MutableLiveData<String?>()
    val userNameLiveData = MutableLiveData<String?>()
    val passwordLiveData = MutableLiveData<String?>()

    private var _loggedInUser = MutableLiveData<User?>()
    val loggedInUser: LiveData<User?> = _loggedInUser

    private var _signedUpInUser = MutableLiveData<User?>()
    val signedUpInUser: LiveData<User?> = _signedUpInUser

    fun getCurrentUser(): String {
        val firebaseUser = firebaseAuth.currentUser
        return firebaseUser?.email.toString()
    }

    fun signUp() {
        val user = checkInputForm()
        if (user != null) {
            firebaseAuth.createUserWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Save user name to realtime database
                        val pushUserKey = database.push().key
                        pushUserKey?.let { key ->
                            database.child(key).setValue(user)
                                .addOnSuccessListener {
                                    _signedUpInUser.value = user
                                    Toast.makeText(getApplication(), "Create and Save Account Successfully", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                .addOnFailureListener { error ->
                                    _signedUpInUser.value = null
                                    Toast.makeText(getApplication(), error.message, Toast.LENGTH_SHORT).show()
                                }
                        }
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

    fun loadUsers() {
        val usersList = mutableListOf<User>()
        // Attach a ValueEventListener to get updates from the "users" node
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Clear the existing list
                usersList.clear()
                // Iterate through the children of the "users" node
                for (userSnapShot in snapshot.children) {
                    val returnedUser = userSnapShot.getValue(User::class.java)
                    returnedUser?.let {
                        usersList.add(it)
                    }
                }
                _returnedUsers.value = usersList
            }
            override fun onCancelled(error: DatabaseError) {
                _returnedUsers.value = null
                Toast.makeText(getApplication(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Validate Function
    private fun checkInputForm(): User? {
        val email = emailLiveData.value
        var isValidEmail = isEmailValid(email)

        val userName = userNameLiveData.value
        var isUserNameValid = isValidUsername(userName)

        val password = passwordLiveData.value
        var isValidPassword = isPasswordValid(password)

        if (!isValidEmail || !isValidPassword) {
            if (!isValidEmail) {
                showToast("Invalid Email!")
            }
            if (!isUserNameValid) {
                showToast("Invalid User Name")
            }
            if (!isValidPassword) {
                showToast("Invalid Password!")
            }
            return null
        }
        return User(email, userName, password, "null")
    }

    private fun isEmailValid(email: String?): Boolean {
        if (email != null) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return true
            }
        }
        return false
    }

    private fun isValidUsername(username: String?): Boolean {
        // Define your validation criteria here
        val regex = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
        if(username != null) {
            return regex.matches(username)
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