package com.phongnn.firebaseloginsignup.data

data class User(
    val email: String?,
    val userName: String?,
    val password: String?,
    val avatarUrl: String?
) {
    // Required empty constructor for Firebase
    constructor() : this("", "", "", "")
}