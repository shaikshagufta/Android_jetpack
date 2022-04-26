package com.example.mvvmdogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmdogs.model.DogBreed

class DetailViewModel: ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()
//in future we will get the info from the room database instead of hard coding as follows
    fun fetch() {
        val dog = DogBreed("1", "Corgi","15 years","breedGroup", "bredFor", "temperament", "")
        dogLiveData.value = dog
    }
}