package com.example.mvvmdogs.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
* this db class will access the db for us,
* for suppose if we needed to access the db on multiple occasions,
* there maybe a case where 2 thread try to access the db at the same time ,
* on different backgroundThreads with conflicting operations on data.=>app crash

*So to avoid this we use a Single Object(Singleton Class) that can access the db at a specific time

*Singleton is a pattern of development where we only have one instance of a class;
* Where ever or how many evr object try to access the db there will only be one instance of this db class
* */

@Database(entities = arrayOf(DogBreed::class), version = 1)
abstract class DogDatabase: RoomDatabase() {
    abstract fun dogDao(): DogDao//returns our interface

    companion object {//creates static functions and variables that can be accessed from outside scope of this class(DogDatabase)
        @Volatile private var instance: DogDatabase? = null
        //Volatile - meaning that writes to this field are immediately made visible to other threads.
        private val LOCK = Any()

        //to invoke the db(DogDatabase) to create the instance
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {// elvis Operator-  ?:
            // synchronized - if multiple threads are trying access at the same time only one will be able to do so
            instance ?: buildDatabase(context).also {//create the instance
                instance = it//attach the instance to the instance var and return it to the invoker of invoke()
            }
        }

        //build a database to create an instance based on the context
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, //regular context is volatile and can be null(for instance when the user rotates the device)
            DogDatabase::class.java,//pass the DogDatabase
            "dogdatabase"//naming the database
        ).build()
    }
}