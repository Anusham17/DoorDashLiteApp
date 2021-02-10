package com.example.doordash

import android.app.Application
import com.example.doordash.dagger.DoorDashComponent
import com.example.doordash.dagger.DaggerDoorDashComponent

class DoorDashApplication : Application() {
    val doorDashComponent: DoorDashComponent by lazy {
        DaggerDoorDashComponent.factory().create(applicationContext)
    }
}