package com.example.mvvmdogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmdogs.model.DogBreed
import com.example.mvvmdogs.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel: ViewModel() {

    private val dogsService = DogsApiService()
    //allows us to observe the observable(Single) without having to worry about disposing it,
    // to avoid memory leaks due to observing or waiting for an Observable(Single) when the app is destroyed
    private val disposable = CompositeDisposable()

    //provides livedata to the actual List<DogBreed> from the data source
    val dogs = MutableLiveData<List<DogBreed>>()
    //another livedata which will notify to the listeners of ViewModel if any error occurred
    val dogsLoadError = MutableLiveData<Boolean>()//true=error
    //another livedata which will notify to the listeners of ViewModel if the data is loading (with no error)
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromRemote()
    }

    //uses Retrofit Service to retrieve data from backEnd Api
    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            //returns us the Single(Observable)
            dogsService.getDogs()
                //using a new bg thread for retrieving info
                .subscribeOn(Schedulers.newThread())
                //to display it we need it back on Main Thread instead on BackgroundThread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>() {
                    // we update our MutableLiveData
                    override fun onSuccess(dogList: List<DogBreed>) {
                        //get the list the dogBreed when success
                        dogs.value = dogList
                        dogsLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        //get an error msg when error
                        dogsLoadError.value = true
                        loading.value = false
                        //to have a trace of the info that's being displayed in logs
                        e.printStackTrace()
                    }
                })
        )
    }

    // to avoid memory leaks due to observing or waiting for an Observable(Single) when the app is destroyed
    override fun onCleared() {
        super.onCleared()
        disposable.clear()//to clean up
    }
}

