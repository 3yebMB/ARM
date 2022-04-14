package dev.m13d.rickandmorty.di

import android.content.Context
import dev.m13d.rickandmorty.di.components.AppComponent
import dev.m13d.rickandmorty.di.components.DaggerAppComponent
import dev.m13d.rickandmorty.di.modules.AppModule
import dev.m13d.rickandmorty.di.modules.RestModule
import dev.m13d.rickandmorty.features.characters.di.CharacterFragmentComponent
import dev.m13d.rickandmorty.features.characters.di.CharactersFragmentComponent
import dev.m13d.rickandmorty.features.episodes.di.EpisodeFragmentComponent
import dev.m13d.rickandmorty.features.episodes.di.EpisodesFragmentComponent
import dev.m13d.rickandmorty.features.locations.di.LocationFragmentComponent
import dev.m13d.rickandmorty.features.locations.di.LocationsFragmentComponent

object Injector {

    private lateinit var appComponent: AppComponent

    val charactersFragmentComponent: CharactersFragmentComponent
        get() {
            return appComponent.charactersFragmentComponent
        }
    val characterFragmentComponent: CharacterFragmentComponent
        get() {
            return appComponent.characterFragmentComponent
        }

    val locationsFragmentComponent: LocationsFragmentComponent
        get() {
            return appComponent.locationsFragmentComponent
        }
    val locationFragmentComponent: LocationFragmentComponent
        get() {
            return appComponent.locationFragmentComponent
        }

    val episodesFragmentComponent: EpisodesFragmentComponent
        get() {
            return appComponent.episodesFragmentComponent
        }
    val episodeFragmentComponent: EpisodeFragmentComponent
        get() {
            return appComponent.episodeFragmentComponent
        }


    internal fun initAppComponent(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context))
            .restModule(RestModule())
            .build()
    }

}
