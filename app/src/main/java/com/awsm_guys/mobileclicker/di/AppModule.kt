package com.awsm_guys.mobileclicker.di

import android.content.Context
import com.awsm_guys.mobileclicker.primitivestore.PrimitiveStore
import com.awsm_guys.mobileclicker.primitivestore.SharedPreferencesPrimitiveStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (private val context: Context){
    @Provides @Singleton
    fun provideAppContext() : Context = context

    @Provides @Singleton
    fun providePrimitiveStore() : PrimitiveStore = SharedPreferencesPrimitiveStore(context)
}