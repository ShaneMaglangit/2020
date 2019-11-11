package com.shanemaglangit.a2020.rest

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RestViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestViewModel::class.java)) {
            return RestViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}