package com.example.shotaro_kumagai

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel() {
    val userImage = MutableLiveData<Bitmap>()
}