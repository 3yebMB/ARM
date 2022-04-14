package dev.m13d.rickandmorty.features.episodes.di

import androidx.lifecycle.ViewModel
import dev.m13d.rickandmorty.di.annotation.ViewModelKey
import dev.m13d.rickandmorty.features.episodes.ui.EpisodesFragment
import dev.m13d.rickandmorty.features.episodes.viewModel.EpisodesViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [EpisodesFragmentModule::class])
interface EpisodesFragmentComponent {
    fun inject(episodesFragment: EpisodesFragment)
}

@Module
interface EpisodesFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesViewModel::class)
    fun bindViewModel(viewModel: EpisodesViewModel): ViewModel

}
