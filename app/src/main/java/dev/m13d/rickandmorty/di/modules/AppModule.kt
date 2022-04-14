package dev.m13d.rickandmorty.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.m13d.rickandmorty.data.database.DatabaseStorage
import dev.m13d.rickandmorty.data.repository.Repository
import dev.m13d.rickandmorty.domain.interactors.*
import dev.m13d.rickandmorty.domain.repository.IRepository
import javax.inject.Singleton

@Module(includes = [AppModule.InnerAppModule::class])
class AppModule(private val context: Context) {

    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideDatabaseStorage() =
        Room.databaseBuilder(
            context,
            DatabaseStorage::class.java,
            DatabaseStorage.RICK_AND_MORTY_DATA_BASE
        ).build()

    @Module
    interface InnerAppModule {

        @Binds
        @Singleton
        fun provideRepository(repository: Repository): IRepository

        @Singleton
        @Binds
        fun provideCharacterInteractor(characterInteractor: CharacterInteractor): ICharacterInteractor

        @Singleton
        @Binds
        fun provideEpisodeInteractor(episodeInteractor: EpisodeInteractor): IEpisodeInteractor

        @Singleton
        @Binds
        fun provideLocationInteractor(locationInteractor: LocationInteractor): ILocationInteractor

    }
}
