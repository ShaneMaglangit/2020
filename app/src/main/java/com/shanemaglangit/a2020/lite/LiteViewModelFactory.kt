package com.shanemaglangit.a2020.lite

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LiteViewModelFactory  (private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LiteViewModel::class.java)) {
            return LiteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}