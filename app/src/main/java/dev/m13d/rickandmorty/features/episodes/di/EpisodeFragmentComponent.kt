package dev.m13d.rickandmorty.features.episodes.di

import androidx.lifecycle.ViewModel
import dev.m13d.rickandmorty.di.annotation.ViewModelKey
import dev.m13d.rickandmorty.features.episodes.episode.EpisodeFragment
import dev.m13d.rickandmorty.features.episodes.episode.EpisodeViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [EpisodeFragmentModule::class])
interface EpisodeFragmentComponent {
    fun inject(episodeFragment: EpisodeFragment)
}

@Module
interface EpisodeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(EpisodeViewModel::class)
    fun bindViewModel(viewModel: EpisodeViewModel): ViewModel

}
