package dev.m13d.rickandmorty.di.components

import android.content.Context
import dev.m13d.rickandmorty.App
import dev.m13d.rickandmorty.di.modules.AppModule
import dev.m13d.rickandmorty.di.modules.RestModule
import dev.m13d.rickandmorty.di.modules.ViewModelModule
import dev.m13d.rickandmorty.features.characters.di.CharacterFragmentComponent
import dev.m13d.rickandmorty.features.characters.di.CharactersFragmentComponent
import dev.m13d.rickandmorty.features.episodes.di.EpisodeFragmentComponent
import dev.m13d.rickandmorty.features.episodes.di.EpisodesFragmentComponent
import dev.m13d.rickandmorty.features.locations.di.LocationFragmentComponent
import dev.m13d.rickandmorty.features.locations.di.LocationsFragmentComponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RestModule::class,
    ViewModelModule::class
])
interface AppComponent {

    val context: Context

    val charactersFragmentComponent: CharactersFragmentComponent
    val characterFragmentComponent: CharacterFragmentComponent

    val locationsFragmentComponent: LocationsFragmentComponent
    val locationFragmentComponent: LocationFragmentComponent

    val episodesFragmentComponent: EpisodesFragmentComponent
    val episodeFragmentComponent: EpisodeFragmentComponent
}
