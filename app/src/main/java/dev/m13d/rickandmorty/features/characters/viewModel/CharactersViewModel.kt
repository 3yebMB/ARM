package dev.m13d.rickandmorty.features.characters.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.domain.entities.CharactersFilter
import dev.m13d.rickandmorty.domain.interactors.ICharacterInteractor
import dev.m13d.rickandmorty.features.base.BaseViewModel
import dev.m13d.rickandmorty.features.characters.ui.CharactersFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val characterInteractor: ICharacterInteractor
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

    fun loadCharacters(page: Int, filter: CharactersFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val charactersEntityResult = characterInteractor.getAllCharacters(page, filter)
            CharactersFragment.pages = charactersEntityResult.info.pages
            updateAppropriateLiveData(charactersEntityResult.results, 0)
        }
    }

    fun reloadCharacters(filter: CharactersFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val charactersEntityResult = characterInteractor.getAllCharacters(1, filter)
            CharactersFragment.pages = charactersEntityResult.info.pages
            updateAppropriateLiveData(charactersEntityResult.results, 1)
        }
    }

    private fun updateAppropriateLiveData(characters: List<Character>, flag: Int) {
        if (!characters.isNullOrEmpty()) {
            onResultSuccess(characters, flag)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(characters: List<Character>, flag: Int) {
        when (flag) {
            0 ->
                if (!charactersMutableLiveData.value.isNullOrEmpty()) {
                    charactersMutableLiveData.value =
                        charactersMutableLiveData.value?.plus(characters)
                } else {
                    charactersMutableLiveData.value = characters
                }
            1 -> charactersMutableLiveData.value = characters
        }
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
