package com.example.mvvmdogs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
/*
we use application context instead of activity context because of volatility of activityContext for retrieving data from db.
so we inherit the baseClass from AndroidViewModel which takes applicationContext as a parameter unlike ViewModel with activityContext as parameter.

CoroutineScope-allows us to have coroutines in our BaseViewModel
*/

    private val job = Job()//we have a Job that is running

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main//when the job is finished, we return to our main thread

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}