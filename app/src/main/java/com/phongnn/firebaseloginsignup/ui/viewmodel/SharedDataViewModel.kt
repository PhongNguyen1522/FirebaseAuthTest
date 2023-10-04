package com.phongnn.firebaseloginsignup.ui.viewmodel

import androidx.lifecycle.ViewModel

class SharedDataViewModel<T> : ViewModel() {
    var sharedData: T? = null
}