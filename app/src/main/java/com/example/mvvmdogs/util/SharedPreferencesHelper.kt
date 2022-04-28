package com.example.mvvmdogs.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

/*
    we need to store this data in a db with the time of retrieval
    we also set the lifetime of that stored data so that
    if we retrieve the data before that lifetime again we can get it from the db(storage)
    otherwise from the remote Api
    (this time Of Retrieval can be stored locally using Shared Prefs)

    we do so with the help of a separate class to help us access the shared prefs
     which like DogDatabase() is a Singleton-a pattern of development where we have only one instance of a class;

*/

class SharedPreferencesHelper {

    companion object{

        private const val PREF_TIME = "Pref Time"//'Pref Time'-name of the variable that we want to store in the SharedPreferences

        private var prefs: SharedPreferences? = null//by default

        @Volatile private var instance: SharedPreferencesHelper? = null//default
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance ?: synchronized(LOCK) {
            instance ?: buildHelper(context).also {//instantiate the shared Preferences
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()

        }
    }
    fun saveUpdateTime(time: Long) {
      //we use a kotlin extension functions on the Shared Preferences
        prefs?.edit(commit = true) {//edit is an extension fun designed by google specifically for working with sharedPrefs
            putLong(PREF_TIME, time)
        }
    }
}