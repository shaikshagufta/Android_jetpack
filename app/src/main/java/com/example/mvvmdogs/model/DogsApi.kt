package com.example.mvvmdogs.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")//for Retrofit to know how to use this method getDogs() this keyword is used
    fun getDogs() : Single<List<DogBreed>>
}