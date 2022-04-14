package dev.m13d.rickandmorty.domain.interactors

import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.domain.entities.Characters
import dev.m13d.rickandmorty.domain.entities.CharactersFilter
import dev.m13d.rickandmorty.domain.repository.IRepository
import javax.inject.Inject

interface ICharacterInteractor {

    suspend fun getAllCharacters(page: Int, filter: CharactersFilter): Characters

    suspend fun getCharacterById(id: Int): Character

    suspend fun getCharactersById(id: String): List<Character>

}

class CharacterInteractor @Inject constructor(private val repository: IRepository) : ICharacterInteractor {

    override suspend fun getAllCharacters(page: Int, filter: CharactersFilter) = repository.getAllCharacters(page, filter)

    override suspend fun getCharacterById(id: Int): Character = repository.getCharacterById(id)

    override suspend fun getCharactersById(id: String): List<Character> = repository.getCharactersById(id)

}
