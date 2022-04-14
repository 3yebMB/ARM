package dev.m13d.rickandmorty.domain.interactors

import dev.m13d.rickandmorty.domain.entities.Location
import dev.m13d.rickandmorty.domain.entities.Locations
import dev.m13d.rickandmorty.domain.entities.LocationsFilter
import dev.m13d.rickandmorty.domain.repository.IRepository
import javax.inject.Inject

interface ILocationInteractor {

    suspend fun getAllLocations(page: Int, filter: LocationsFilter): Locations

    suspend fun getLocationById(id: Int): Location

    suspend fun getLocationsById(id: String): List<Location>
}

class LocationInteractor @Inject constructor(private val repository: IRepository) :
    ILocationInteractor {

    override suspend fun getAllLocations(page: Int, filter: LocationsFilter) = repository.getAllLocations(page, filter)

    override suspend fun getLocationById(id: Int) = repository.getLocationById(id)

    override suspend fun getLocationsById(id: String) = repository.getLocationsById(id)

}
