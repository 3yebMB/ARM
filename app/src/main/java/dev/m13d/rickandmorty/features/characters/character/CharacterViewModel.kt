package dev.m13d.rickandmorty.features.characters.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.domain.entities.Episode
import dev.m13d.rickandmorty.domain.interactors.ICharacterInteractor
import dev.m13d.rickandmorty.domain.interactors.IEpisodeInteractor
import dev.m13d.rickandmorty.features.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val episodeInteractor: IEpisodeInteractor,
    private val characterInteractor: ICharacterInteractor
) : BaseViewModel() {

    private val episodesMutableLiveData: MutableLiveData<List<Episode>> =
        MutableLiveData()
    val episodesLiveData: LiveData<List<Episode>>
        get() = episodesMutableLiveData

    private val isErrorMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean>
        get() = isErrorMutableLiveData

    private val isLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingLiveData: LiveData<Boolean>
        get() = isLoadingMutableLiveData

    fun loadEpisodes(episodesList: List<String>) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            var episodes = ""
            for (episode in episodesList) {
                val id = episode.split("/").last()
                episodes += "$id,"
            }
            val episodesEntityResult = episodeInteractor.getEpisodesById(episodes)
            updateAppropriateLiveData(episodesEntityResult)
        }
    }

    fun loadCharacter(characterId: Int): Character {
        var character: Character? = null
        runBlocking {
            val getCharacter = async {
                characterInteractor.getCharacterById(characterId)
            }
            character = getCharacter.await()

        }
        return character!!
    }

    private fun updateAppropriateLiveData(episodes: List<Episode>) {
        if (!episodes.isNullOrEmpty()) {
            onResultSuccess(episodes)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(episodes: List<Episode>) {
        episodesMutableLiveData.value = episodes

        isLoadingLiveData(false)
    }

    private fun onResultError() {
        viewModelScope.launch {
            delay(300)
            isLoadingLiveData(false)
        }.invokeOnCompletion {
            isErrorMutableLiveData.value = true
        }
    }

    private fun isLoadingLiveData(isLoading: Boolean) {
        this.isLoadingMutableLiveData.value = isLoading
    }
}
