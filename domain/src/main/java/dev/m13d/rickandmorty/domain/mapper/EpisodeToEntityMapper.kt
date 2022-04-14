package dev.m13d.rickandmorty.domain.mapper

import dev.m13d.rickandmorty.domain.entities.Episode
import dev.m13d.rickandmorty.domain.entities.EpisodeEntity

object EpisodeToEntityMapper : BaseMapper<List<Episode>, List<EpisodeEntity>> {
    override fun map(type: List<Episode>?): List<EpisodeEntity> {
        return type?.map {
            EpisodeEntity(
                id = it.id,
                name = it.name,
                date = it.date,
                episode = it.episode,
                url = it.url
            )
        } ?: listOf()
    }

    fun mapOne(type: Episode): EpisodeEntity {
        return EpisodeEntity(
            id = type.id,
            name = type.name,
            date = type.date,
            episode = type.episode,
            url = type.url
        )
    }

}

object EntityToEpisodeMapper : BaseMapper<List<EpisodeEntity>, List<Episode>> {
    override fun map(type: List<EpisodeEntity>?): List<Episode> {
        return type?.map {
            Episode(
                id = it.id,
                name = it.name,
                date = it.date,
                episode = it.episode,
                url = it.url,
                characters = null
            )
        } ?: listOf()
    }

    fun mapOne(type: EpisodeEntity): Episode {
        return Episode(
            id = type.id,
            name = type.name,
            date = type.date,
            episode = type.episode,
            url = type.url,
            characters = null
        )
    }
}
