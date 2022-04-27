package com.example.mvvmdogs.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DogsApiService {

    ///to match the base url with the end points or end point
    private val BASE_URL = "https://raw.githubusercontent.com"

    // object created by Retrofit used to allow us to access the backend endPoint
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
            //Converting the Gson to a Single

        //google Library// converts the Json file in the api into our dataModel class format
        .addConverterFactory(GsonConverterFactory.create())
        //converts the list of the objects in the dataModel class to an observable(Single)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            //I- Single- Observable that emits data once and finishes
        .build()
        //build based on the DogsApi Interface i.e, methods defined in it
        .create(DogsApi::class.java)

    //methods to return the data required from each method call in the Interface
    fun getDogs() : Single<List<DogBreed>> {//observable that returns the info from the backend
        return api.getDogs()
    }
}