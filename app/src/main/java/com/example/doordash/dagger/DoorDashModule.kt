package com.example.doordash.dagger

import com.example.doordash.repository.DoorDashRepository
import com.example.doordash.repository.DoorDashRepositoryImpl
import com.example.doordash.repository.remote.api.DoorDashService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class DoorDashModule {
    /**
     *  Supplies single instance of retrofit for entire application
     */
    @Singleton
    @Provides
    fun getRetrofit() = Retrofit.Builder()
        .baseUrl("https://api.doordash.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Supplies doordash retrofit service
     */
    @Provides
    fun getDoorDashApi(retrofit: Retrofit): DoorDashService = retrofit.create(
        DoorDashService::class.java)

    @Provides
    fun getDoorDashRepository(service: DoorDashService): DoorDashRepository =
        DoorDashRepositoryImpl(service)
}