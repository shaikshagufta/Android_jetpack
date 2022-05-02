package com.example.mvvmdogs.view

import android.view.View


/*
*In Databinding ,to add method calls in Layouts , like we access variables we need a listener
* this is a listener  which is basically an interface that listens for events and causes some result when an event is triggered
**/
interface DogClickListener {
    fun onDogClicked(v: View, dogUuid: Int)
}