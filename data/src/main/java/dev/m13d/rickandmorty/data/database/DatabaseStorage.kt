package dev.m13d.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.m13d.rickandmorty.data.database.dao.RickAndMortyDao
import dev.m13d.rickandmorty.domain.entities.CharacterEntity
import dev.m13d.rickandmorty.domain.entities.EpisodeEntity
import dev.m13d.rickandmorty.domain.entities.LocationEntity

@Database(
    entities = [CharacterEntity::class, LocationEntity::class, EpisodeEntity::class],
    version = 1
)
abstract class DatabaseStorage : RoomDatabase() {

    abstract val rickAndMortyDao: RickAndMortyDao

    companion object {
        const val RICK_AND_MORTY_DATA_BASE = "RICK_AND_MORTY_DATA_BASE"
    }

}
