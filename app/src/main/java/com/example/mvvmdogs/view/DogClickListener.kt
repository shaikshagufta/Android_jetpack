package com.example.mvvmdogs.view

import android.view.View

//interface that listens for events and causes some result when an event is triggered

interface DogClickListener {
    fun onDogClicked(v: View, dogUuid: Int)
}