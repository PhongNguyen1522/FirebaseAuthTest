package com.phongnn.firebaseloginsignup.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.phongnn.firebaseloginsignup.data.Comment
import com.phongnn.firebaseloginsignup.data.User
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ChatViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("messages")

    // Observe what users type
    val messageLiveData = MutableLiveData<String?>()

    // Create a live data to observe returned comments list
    private var _returnedComments = MutableLiveData<List<Comment>?>()
    val returnedComments: LiveData<List<Comment>?> = _returnedComments

    // Get a ListUsersPage object, which represents a page of user data
    fun sendMessage(senderEmail: String, receiveEmail: String) {

        val currentTime = getCurrentTime()

        val newComment = Comment(
            senderEmail,
            receiveEmail,
            messageLiveData.value ?: "",
            currentTime
        )
        // Push the new message to the "messages" node in the database
        val commentKey = database.push().key
        commentKey?.let {
            database.child(it).setValue(newComment)
                .addOnSuccessListener {
                    Toast.makeText(getApplication(), "Comment Successfully", Toast.LENGTH_SHORT)
                        .show()
                    loadMessages(senderEmail, receiveEmail)
                }
                .addOnFailureListener { error ->
                    Toast.makeText(getApplication(), error.message, Toast.LENGTH_SHORT).show()
                }
        }

    }

    fun loadMessages(senderEmail: String, receiveEmail: String) {
        val commentsList = mutableListOf<Comment>()
        // Attach a ValueEventListener to get updates from the "comments" node
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing list
                commentsList.clear()

                // Iterate through the children of the "comments" node
                for (commentSnapshot in snapshot.children) {
                    // Convert each child to a Comment object
                    val comment = commentSnapshot.getValue(Comment::class.java)

                    // Add the comment to the list
                    comment?.let {
                        when {
                            it.senderEmail.equals(senderEmail, ignoreCase = false) &&
                                    it.receiverEmail.equals(receiveEmail, ignoreCase = false) ->
                                commentsList.add(it)

                            it.senderEmail.equals(receiveEmail, ignoreCase = false) &&
                                    it.receiverEmail.equals(senderEmail, ignoreCase = false) ->
                                commentsList.add(it)

                            else -> {
                                // Do nothing
                            }
                        }
                    }

                }
                _returnedComments.value = commentsList
            }

            override fun onCancelled(error: DatabaseError) {
                _returnedComments.value = null
                Toast.makeText(getApplication(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(formatter)
    }

}