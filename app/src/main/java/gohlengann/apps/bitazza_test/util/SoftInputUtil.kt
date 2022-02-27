package gohlengann.apps.bitazza_test.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment

fun Activity.hideKeyboard() {
    val inputMethodManager: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}