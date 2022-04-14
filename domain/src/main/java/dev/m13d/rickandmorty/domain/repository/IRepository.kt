package dev.m13d.rickandmorty.domain.repository

import dev.m13d.rickandmorty.domain.entities.*

interface IRepository {

    suspend fun getAllCharacters(page: Int, filter: CharactersFilter): Characters

    suspend fun getCharacterById(id: Int): Character

    suspend fun getCharactersById(id: String): List<Character>


    suspend fun getAllLocations(page: Int, filter: LocationsFilter): Locations

    suspend fun getLocationById(id: Int): Location

    suspend fun getLocationsById(id: String): List<Location>


    suspend fun getAllEpisodes(page: Int, filter: EpisodesFilter): Episodes

    suspend fun getEpisodeById(id: Int): Episode

    suspend fun getEpisodesById(id: String): List<Episode>

}
