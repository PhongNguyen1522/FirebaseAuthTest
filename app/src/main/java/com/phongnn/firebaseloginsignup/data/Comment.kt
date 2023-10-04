package com.phongnn.firebaseloginsignup.data

data class Comment(
    val senderEmail: String = "",
    val receiverEmail: String = "",
    val text: String = "",
    val time: String = ""
) {
    // Required empty constructor for Firebase
    constructor() : this("", "", "")
}

