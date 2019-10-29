package com.shanemaglangit.countdown

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CountdownViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountdownViewModel::class.java)) {
            return CountdownViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}