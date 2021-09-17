package com.daniilmaster.storageanimal.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.daniilmaster.storageanimal.R

class FilterPrefFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        return
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_filter)
    }

}