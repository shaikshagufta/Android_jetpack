package com.example.mvvmdogs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    private val job = Job()//we have a Job that is running

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main//when the job is finished, we return to our main thread

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}