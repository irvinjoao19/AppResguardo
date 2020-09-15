package com.dsige.dominion.appresguardo.data

import com.dsige.dominion.appresguardo.data.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
    }
}