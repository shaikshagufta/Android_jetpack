package com.example.mvvmdogs.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao//data access object is an interface through which we will access objects in the db
//defines what kind of functions we can perform on our db
// like for Retrofit we used DogsApi interface and DogsApi , for Room we use Dao and a Service
interface DogDao {
    //allow insert
    @Insert
    //suspend - to allow to use thread other than the mainThread
    suspend fun  insertAll(vararg dogs: DogBreed): List<Long>//Long-uuid

    @Query("SELECT * FROM dogbreed")
    suspend fun getAllDogs(): List<DogBreed>

    //to retrieve a single dogBreed element based on uuid
    @Query("SELECT * FROM dogbreed WHERE uuid = :dogId")
    suspend fun getDog(dogId: Int): DogBreed//Int becoz uuid: Int

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllDogs()
}