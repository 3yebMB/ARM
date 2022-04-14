package dev.m13d.rickandmorty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.m13d.rickandmorty.domain.entities.CharacterEntity
import dev.m13d.rickandmorty.domain.entities.EpisodeEntity
import dev.m13d.rickandmorty.domain.entities.LocationEntity

@Dao
interface RickAndMortyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE (:name IS NULL OR name LIKE :name) AND (:status IS NULL OR status LIKE :status) AND (:gender IS NULL OR gender LIKE :gender) AND (:species IS NULL OR species LIKE :species)")
    suspend fun getAllCharacters(
        name: String?,
        status: String?,
        gender: String?,
        species: String?
    ): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id =:id")
    suspend fun getCharacterById(id: Int): CharacterEntity

    @Query("SELECT * FROM characters WHERE id IN (:id)")
    suspend fun getCharactersById(id: String): List<CharacterEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllLocations(locations: List<LocationEntity>)

    @Query("SELECT * FROM locations WHERE (:name IS NULL OR name LIKE :name) AND (:type IS NULL OR type LIKE :type)")
    suspend fun getAllLocations(name: String?, type: String?): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE id =:id")
    suspend fun getLocationById(id: Int): LocationEntity

    @Query("SELECT * FROM locations WHERE id IN (:id)")
    suspend fun getLocationsById(id: String): List<LocationEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllEpisodes(episodes: List<EpisodeEntity>)

    @Query("SELECT * FROM episodes WHERE (:name IS NULL OR name LIKE :name) AND (:season IS NULL OR episode LIKE :season)")
    suspend fun getAllEpisodes(name: String?, season: String?): List<EpisodeEntity>

    @Query("SELECT * FROM episodes WHERE id =:id")
    suspend fun getEpisodeById(id: Int): EpisodeEntity

    @Query("SELECT * FROM episodes WHERE id IN (:id)")
    suspend fun getEpisodesById(id: String): List<EpisodeEntity>

}
