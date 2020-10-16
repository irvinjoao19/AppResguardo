package com.dsige.dominion.appresguardo.data.module

import com.dsige.dominion.appresguardo.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

abstract class FragmentBindingModule {

    @Module
    abstract class Main {
        @ContributesAndroidInjector
        internal abstract fun providMainFragment(): MainFragment

    }

    @Module
    abstract class Form {
        @ContributesAndroidInjector
        internal abstract fun providGeneralFragment(): GeneralFragment

        @ContributesAndroidInjector
        internal abstract fun providFirmFragment(): FirmFragment
    }

}