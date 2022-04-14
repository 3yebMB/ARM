package dev.m13d.rickandmorty.features.locations.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.domain.entities.Episode
import dev.m13d.rickandmorty.domain.entities.Location
import dev.m13d.rickandmorty.domain.interactors.ICharacterInteractor
import dev.m13d.rickandmorty.domain.interactors.IEpisodeInteractor
import dev.m13d.rickandmorty.domain.interactors.ILocationInteractor
import dev.m13d.rickandmorty.features.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val characterInteractor: ICharacterInteractor,
    private val locationInteractor: ILocationInteractor
) : BaseViewModel() {

    private val charactersMutableLiveData: MutableLiveData<List<Character>> =
        MutableLiveData()
    val charactersLiveData: LiveData<List<Character>>
        get() = charactersMutableLiveData

    private val isErrorMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isErrorLiveData: LiveData<Boolean>
        get() = isErrorMutableLiveData

    private val isLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingLiveData: LiveData<Boolean>
        get() = isLoadingMutableLiveData

    fun loadCharacters(charactersList: List<String>) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            var characters = ""
            for (character in charactersList) {
                val id = character.split("/").last()
                characters += "$id,"
            }
            val charactersEntityResult = characterInteractor.getCharactersById(characters)
            updateAppropriateLiveData(charactersEntityResult)
        }
    }

    fun loadLocation(locationId: Int): Location {
        var location: Location? = null
        runBlocking {
            val getLocation = async {
                locationInteractor.getLocationById(locationId)
            }
            location = getLocation.await()

        }
        return location!!
    }

    private fun updateAppropriateLiveData(characters: List<Character>) {
        if (!characters.isNullOrEmpty()) {
            onResultSuccess(characters)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(characters: List<Character>) {
        charactersMutableLiveData.value = characters

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
