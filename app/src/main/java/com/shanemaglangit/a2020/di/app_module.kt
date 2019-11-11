package com.shanemaglangit.a2020.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { provideSharedPreferences(androidContext()) }
}

private fun provideSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)