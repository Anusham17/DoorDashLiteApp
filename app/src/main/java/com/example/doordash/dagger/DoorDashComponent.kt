package com.example.doordash.dagger

import android.content.Context
import com.example.doordash.ui.fragment.StoreDetailFragment
import com.example.doordash.ui.fragment.StoreFeedFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Component(
    modules = [DoorDashModule::class,
        AndroidSupportInjectionModule::class]
)
@Singleton
interface DoorDashComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DoorDashComponent
    }

    fun inject(storeFeedFragment: StoreFeedFragment)
    fun inject(storeDetailFragment: StoreDetailFragment)
}