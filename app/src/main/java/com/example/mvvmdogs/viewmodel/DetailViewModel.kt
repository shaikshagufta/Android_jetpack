package com.example.mvvmdogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdogs.model.DogBreed
import com.example.mvvmdogs.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()
    //we get the info from the room database instead of hard coding as follows
    fun fetch(uuid: Int) {
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(uuid)
            dogLiveData.value = dog
        }
    }
}