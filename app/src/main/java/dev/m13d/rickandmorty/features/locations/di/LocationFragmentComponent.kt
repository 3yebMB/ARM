package dev.m13d.rickandmorty.features.locations.di

import androidx.lifecycle.ViewModel
import dev.m13d.rickandmorty.di.annotation.ViewModelKey
import dev.m13d.rickandmorty.features.locations.location.LocationFragment
import dev.m13d.rickandmorty.features.locations.location.LocationViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [LocationFragmentModule::class])
interface LocationFragmentComponent {
    fun inject(locationFragment: LocationFragment)
}

@Module
interface LocationFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    fun bindViewModel(viewModel: LocationViewModel): ViewModel

}
