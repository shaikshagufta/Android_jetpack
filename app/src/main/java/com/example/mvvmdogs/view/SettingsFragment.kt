package com.example.mvvmdogs.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.mvvmdogs.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}