package dev.m13d.rickandmorty.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.m13d.rickandmorty.di.ViewModelFactory
import dev.m13d.rickandmorty.di.annotation.ViewModelKey
import dev.m13d.rickandmorty.features.characters.viewModel.CharactersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
