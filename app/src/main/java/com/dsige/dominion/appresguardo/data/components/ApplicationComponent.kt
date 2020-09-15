package com.dsige.dominion.appresguardo.data.components

import android.app.Application
import com.dsige.dominion.appresguardo.data.App
import com.dsige.dominion.appresguardo.data.module.ActivityBindingModule
import com.dsige.dominion.appresguardo.data.module.DataBaseModule
import com.dsige.dominion.appresguardo.data.module.RetrofitModule
import com.dsige.dominion.appresguardo.data.module.ServicesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        RetrofitModule::class,
        DataBaseModule::class,
        ServicesModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}