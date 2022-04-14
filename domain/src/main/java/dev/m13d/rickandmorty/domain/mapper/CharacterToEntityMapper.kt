package dev.m13d.rickandmorty.domain.mapper

import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.domain.entities.CharacterEntity
import dev.m13d.rickandmorty.domain.entities.CharacterLocation

object CharacterToEntityMapper : BaseMapper<List<Character>, List<CharacterEntity>> {
    override fun map(type: List<Character>?): List<CharacterEntity> {
        return type?.map {
            CharacterEntity(
                id = it.id,
                name = it.name,
                species = it.species,
                type = it.type,
                status = it.status,
                gender = it.gender,
                image = it.image,
                origin = it.origin.name,
                originUrl = it.origin.url,
                location = it.location.name,
                locationUrl = it.location.url
            )
        } ?: listOf()
    }
}

object EntityToCharacterMapper : BaseMapper<List<CharacterEntity>, List<Character>> {
    override fun map(type: List<CharacterEntity>?): List<Character> {
        return type?.map {
            Character(
                id = it.id,
                name = it.name,
                species = it.species,
                type = it.type,
                status = it.status,
                gender = it.gender,
                image = it.image,
                origin = CharacterLocation(it.origin, it.originUrl),
                location = CharacterLocation(it.location, it.locationUrl),
                episode = null
            )
        } ?: listOf()
    }

    fun mapOne(type: CharacterEntity): Character {
        return Character(
            id = type.id,
            name = type.name,
            species = type.species,
            type = type.type,
            status = type.status,
            gender = type.gender,
            image = type.image,
            origin = CharacterLocation(type.origin, type.originUrl),
            location = CharacterLocation(type.location, type.locationUrl),
            episode = null
        )
    }
}
