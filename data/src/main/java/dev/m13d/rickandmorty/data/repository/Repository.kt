package dev.m13d.rickandmorty.data.repository

import dev.m13d.rickandmorty.data.database.DatabaseStorage
import dev.m13d.rickandmorty.data.network.RickAndMortyApi
import dev.m13d.rickandmorty.domain.entities.*
import dev.m13d.rickandmorty.domain.mapper.*
import dev.m13d.rickandmorty.domain.repository.IRepository
import javax.inject.Inject

class Repository @Inject constructor(
    private val databaseStorage: DatabaseStorage,
    private val rickAndMortyApi: RickAndMortyApi
) : IRepository {

    override suspend fun getAllCharacters(page: Int, filter: CharactersFilter) = try {
        val res = rickAndMortyApi.getAllCharacters(
            page,
            filter.name,
            filter.status,
            filter.gender,
            filter.species
        )
        databaseStorage.rickAndMortyDao.saveAllCharacters(CharacterToEntityMapper.map(res.results))
        res
    } catch (e: Exception) {
        val list = EntityToCharacterMapper.map(
            databaseStorage.rickAndMortyDao.getAllCharacters(
                if (filter.name.isNullOrEmpty()) filter.name else "%${filter.name}%",
                filter.status,
                filter.gender,
                filter.species
            )
        )
        Characters(list, Info(1))
    }

    override suspend fun getCharacterById(id: Int): Character = try {
        rickAndMortyApi.getCharacterById(id)
    } catch (e: Exception) {
        EntityToCharacterMapper.mapOne(databaseStorage.rickAndMortyDao.getCharacterById(id))
    }

    override suspend fun getCharactersById(id: String): List<Character> = try {
        rickAndMortyApi.getCharactersById(id)
    } catch (e: Exception) {
        EntityToCharacterMapper.map(databaseStorage.rickAndMortyDao.getCharactersById(id))
    }


    override suspend fun getAllLocations(page: Int, filter: LocationsFilter) = try {
        val res = rickAndMortyApi.getAllLocations(page, filter.name, filter.type)
        databaseStorage.rickAndMortyDao.saveAllLocations(LocationToEntityMapper.map(res.results))
        res
    } catch (e: Exception) {
        val list = EntityToLocationMapper.map(
            databaseStorage.rickAndMortyDao.getAllLocations(
                if (filter.name.isNullOrEmpty()) filter.name else "%${filter.name}%",
                filter.type
            )
        )
        Locations(list, Info(1))
    }

    override suspend fun getLocationById(id: Int): Location = try {
        rickAndMortyApi.getLocationById(id)
    } catch (e: Exception) {
        EntityToLocationMapper.mapOne(databaseStorage.rickAndMortyDao.getLocationById(id))
    }

    override suspend fun getLocationsById(id: String): List<Location> = try {
        rickAndMortyApi.getLocationsById(id)
    } catch (e: Exception) {
        EntityToLocationMapper.map(databaseStorage.rickAndMortyDao.getLocationsById(id))
    }


    override suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter) = try {
        val res = rickAndMortyApi.getAllEpisodes(page, filter.name, filter.season)
        databaseStorage.rickAndMortyDao.saveAllEpisodes(
            EpisodeToEntityMapper.map(res.results)
        )
        res
    } catch (e: Exception) {
        val list =
            EntityToEpisodeMapper.map(databaseStorage.rickAndMortyDao.getAllEpisodes(
                if (filter.name.isNullOrEmpty()) filter.name else "%${filter.name}%",
                if (filter.season.isNullOrEmpty()) filter.season else "%${filter.season}%"))
        Episodes(list, Info(1))
    }

    override suspend fun getEpisodeById(id: Int): Episode = try {
        rickAndMortyApi.getEpisodeById(id)
    } catch (e: Exception) {
        EntityToEpisodeMapper.mapOne(databaseStorage.rickAndMortyDao.getEpisodeById(id))
    }

    override suspend fun getEpisodesById(id: String): List<Episode> = try {
        rickAndMortyApi.getEpisodesById(id)
    } catch (e: Exception) {
        EntityToEpisodeMapper.map(databaseStorage.rickAndMortyDao.getEpisodesById(id))
    }

}
