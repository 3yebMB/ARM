package dev.m13d.rickandmorty.features.locations.di

import androidx.lifecycle.ViewModel
import dev.m13d.rickandmorty.di.annotation.ViewModelKey
import dev.m13d.rickandmorty.features.locations.ui.LocationsFragment
import dev.m13d.rickandmorty.features.locations.viewModel.LocationsViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [LocationsFragmentModule::class])
interface LocationsFragmentComponent {
    fun inject(locationsFragment: LocationsFragment)
}

@Module
interface LocationsFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationsViewModel::class)
    fun bindViewModel(viewModel: LocationsViewModel): ViewModel

}
