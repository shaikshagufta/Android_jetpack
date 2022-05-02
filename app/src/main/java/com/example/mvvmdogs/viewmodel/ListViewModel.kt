package com.example.mvvmdogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdogs.model.DogBreed
import com.example.mvvmdogs.model.DogDatabase
import com.example.mvvmdogs.model.DogsApiService
import com.example.mvvmdogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application): BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L//5minutes in nanoSecs in var type Long

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

        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime ) {
            fetchFromDataBase()
        } else {
            fetchFromRemote()
        }
    }

    //fun to byPass/skip the cache(local db)
    fun refreshBypassCache() {
        fetchFromRemote()
    }


    private fun fetchFromDataBase() {
        loading.value = true
        //we need a background thread as we are operating the database hence,
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(),"Dogs retrieved from Local database", Toast.LENGTH_SHORT).show()
        }
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

                    override fun onSuccess(dogList: List<DogBreed>) {

                        storeDogsLocally(dogList)

                        Toast.makeText(getApplication(),"Dogs retrieved from Remote end Point", Toast.LENGTH_SHORT).show()
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

    // we update our MutableLiveData
    private fun dogsRetrieved(dogList: List<DogBreed>) {
        //get the list the dogBreed when success
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }


    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()

            dao.deleteAllDogs()
            //to avoid polluting the database with the previous dog info when we arrive 2nd time
            val result = dao.insertAll(*list.toTypedArray())//to get the uuids
            //it gets a list and expands it into individual elements that we can pass to our insertAll() in DogDatabase, there we retrieve a list of uuid of elements
            //assigning those uuids to the right Dog objects
            var i = 0//default
            while (i < list.size) {
                list[i].uuid = result[i].toInt()//assigning i to the corresponding list element
                ++i//incrementing i by 1
            }
            dogsRetrieved(list)
        }
        //stores the time accurate to nano seconds when we have updated the database with  the dog info that was retrieved
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    // to avoid memory leaks due to observing or waiting for an Observable(Single) when the app is destroyed
    override fun onCleared() {
        super.onCleared()
        disposable.clear()//to clean up
    }
}
