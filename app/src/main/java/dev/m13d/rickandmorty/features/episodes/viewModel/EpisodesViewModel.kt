package dev.m13d.rickandmorty.features.episodes.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.m13d.rickandmorty.domain.entities.Episode
import dev.m13d.rickandmorty.domain.entities.EpisodesFilter
import dev.m13d.rickandmorty.domain.interactors.IEpisodeInteractor
import dev.m13d.rickandmorty.features.base.BaseViewModel
import dev.m13d.rickandmorty.features.episodes.ui.EpisodesFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(
    private val episodeInteractor: IEpisodeInteractor
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

    fun loadEpisodes(page: Int, filter: EpisodesFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val episodesEntityResult = episodeInteractor.getAllEpisodes(page, filter)
            EpisodesFragment.pages = episodesEntityResult.info.pages
            updateAppropriateLiveData(episodesEntityResult.results, 0)
        }
    }

    fun reloadEpisodes(filter: EpisodesFilter) {
        isLoadingLiveData(true)
        viewModelScope.launch {
            val episodesEntityResult = episodeInteractor.getAllEpisodes(1, filter)
            EpisodesFragment.pages = episodesEntityResult.info.pages
            updateAppropriateLiveData(episodesEntityResult.results, 1)
        }
    }

    private fun updateAppropriateLiveData(episodes: List<Episode>, flag: Int) {
        if (!episodes.isNullOrEmpty()) {
            onResultSuccess(episodes, flag)
        } else {
            onResultError()
        }
    }

    private fun onResultSuccess(episodes: List<Episode>, flag: Int) {
        when (flag) {
            0 ->
                if (!episodesMutableLiveData.value.isNullOrEmpty()) {
                    episodesMutableLiveData.value = episodesMutableLiveData.value?.plus(episodes)
                } else {
                    episodesMutableLiveData.value = episodes
                }
            1 -> episodesMutableLiveData.value = episodes
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
