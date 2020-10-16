package com.dsige.dominion.appresguardo.data.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsige.dominion.appresguardo.data.viewModel.*
import com.dsige.dominion.appresguardo.data.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UsuarioViewModel::class)
    internal abstract fun bindUserViewModel(usuarioViewModel: UsuarioViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OtViewModel::class)
    internal abstract fun bindOtViewModel(otViewModel: OtViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}