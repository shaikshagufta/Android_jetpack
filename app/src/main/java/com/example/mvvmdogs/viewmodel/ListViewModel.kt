package com.example.mvvmdogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmdogs.model.DogBreed

class ListViewModel: ViewModel() {

    //provides livedata to the actual List<DogBreed> from the data source
    val dogs = MutableLiveData<List<DogBreed>>()
    //another livedata which will notify to the listeners of ViewModel if any error occurred
    val dogsLoadError = MutableLiveData<Boolean>()//true=error
    //another livedata which will notify to the listeners of ViewModel if the data is loading (with no error)
    val loading = MutableLiveData<Boolean>()

    //method
    fun refresh() {
        val dog1 = DogBreed("1", "Corgi","15 years","breedGroup", "bredFor", "temperament", "")
        val dog2 = DogBreed("2", "Labrador","10 years","breedGroup", "bredFor", "temperament", "")
        val dog3 = DogBreed("3", "Rotwailer","20 years","breedGroup", "bredFor", "temperament", "")
        val dogList:ArrayList<DogBreed> = arrayListOf<DogBreed>(dog1,dog2,dog3)

        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false

    }
}