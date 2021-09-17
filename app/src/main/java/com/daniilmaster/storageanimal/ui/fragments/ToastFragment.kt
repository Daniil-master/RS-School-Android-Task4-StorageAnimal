package com.daniilmaster.storageanimal.ui.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(string: String) {
    Toast.makeText(
        requireContext(),
        string,
        Toast.LENGTH_SHORT
    ).show()
}

fun Fragment.showToast(id: Int) {
    Toast.makeText(
        requireContext(),
        id,
        Toast.LENGTH_SHORT
    ).show()
}