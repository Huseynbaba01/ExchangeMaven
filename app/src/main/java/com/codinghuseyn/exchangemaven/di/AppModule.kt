package com.codinghuseyn.exchangemaven.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun test(): String {
        Log.d("MyTagHere", "Lazy initialization of an expensive object")
        return "MyExample"
    }
}