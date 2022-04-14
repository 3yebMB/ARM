package dev.m13d.rickandmorty.domain.mapper

import dev.m13d.rickandmorty.domain.entities.Location
import dev.m13d.rickandmorty.domain.entities.LocationEntity

object LocationToEntityMapper : BaseMapper<List<Location>, List<LocationEntity>> {
    override fun map(type: List<Location>?): List<LocationEntity> {
        return type?.map {
            LocationEntity(
                id = it.id,
                name = it.name,
                type = it.type,
                dimension = it.dimension,
                url = it.url
            )
        } ?: listOf()
    }

    fun mapOne(type: Location): LocationEntity {
        return LocationEntity(
            id = type.id,
            name = type.name,
            type = type.type,
            dimension = type.dimension,
            url = type.url
        )
    }

}

object EntityToLocationMapper : BaseMapper<List<LocationEntity>, List<Location>> {
    override fun map(type: List<LocationEntity>?): List<Location> {
        return type?.map {
            Location(
                id = it.id,
                name = it.name,
                type = it.type,
                dimension = it.dimension,
                url = it.url,
                characters = null
            )
        } ?: listOf()
    }

    fun mapOne(type: LocationEntity): Location {
        return Location(
            id = type.id,
            name = type.name,
            type = type.type,
            dimension = type.dimension,
            url = type.url,
            characters = null
        )
    }
}
