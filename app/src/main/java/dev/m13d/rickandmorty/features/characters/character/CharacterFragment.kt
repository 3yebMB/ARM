package dev.m13d.rickandmorty.features.characters.character

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.domain.entities.Episode
import dev.m13d.rickandmorty.domain.interactors.ICharacterInteractor
import dev.m13d.rickandmorty.domain.interactors.IEpisodeInteractor
import dev.m13d.rickandmorty.R
import dev.m13d.rickandmorty.R.layout.fragment_character
import dev.m13d.rickandmorty.di.Injector
import dev.m13d.rickandmorty.features.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject

private const val CHARACTER_ID = "characterId"

class CharacterFragment : BaseFragment<CharacterViewModel>() {

    companion object {
        fun newInstance(characterId: Int): CharacterFragment {
            val args = Bundle().apply {
                putInt(CHARACTER_ID, characterId)
            }
            return CharacterFragment().apply {
                arguments = args
            }
        }
    }

    interface CharacterCallbacks {
        fun onEpisodeItemSelected(episodeId: Int)
        fun onLocationItemSelected(locationId: Int)
    }

    private var callbacks: CharacterCallbacks? = null

    @Inject
    lateinit var episodeInteractor: IEpisodeInteractor

    @Inject
    lateinit var characterInteractor: ICharacterInteractor

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    private var episodes: List<Episode> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var episodesListAdapter: EpisodesListAdapter
    private var character: Character? = null
    private lateinit var spinner: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CharacterCallbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Injector.characterFragmentComponent.inject(this)
        injectViewModel()
        return inflater.inflate(fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.progress_bar)
        if (character == null) {
            val characterId = arguments?.getInt(CHARACTER_ID)
            character = viewModel.loadCharacter(characterId!!)
            character?.episode?.let { viewModel.loadEpisodes(it) }
        }
        initViews(view)
        observeLiveData()
    }

    private fun initViews(view: View) {
        view.findViewById<TextView>(R.id.characterName).text = character?.name
        view.findViewById<TextView>(R.id.characterSpecies).text = character?.species
        view.findViewById<TextView>(R.id.characterType).text =
            if (character?.type!!.isEmpty()) "None"
            else character?.type
        view.findViewById<TextView>(R.id.characterStatus).text = character?.status
        view.findViewById<TextView>(R.id.characterGender).text = character?.gender
        val location = view.findViewById<TextView>(R.id.characterLocation)
        location.text = character?.location?.name
        location.setOnClickListener {
            callbacks?.onLocationItemSelected(character?.location?.url!!.split("/").last().toInt())
        }
        val origin = view.findViewById<TextView>(R.id.characterOrigin)
        origin.text = character?.origin?.name
        origin.setOnClickListener {
            callbacks?.onLocationItemSelected(character?.origin?.url!!.split("/").last().toInt())
        }

        val image = view.findViewById<ImageView>(R.id.characterImage)
        Glide
            .with(view.context)
            .load(character?.image)
            .into(image)

        initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.divider
            )!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        episodesListAdapter = EpisodesListAdapter()
        episodesListAdapter.onClick = { episode ->
            callbacks?.onEpisodeItemSelected(episode.id)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = episodesListAdapter
            episodesListAdapter.setData(episodes)
        }
    }

    private fun observeLiveData() {
        viewModel.episodesLiveData.observe(requireActivity(), Observer(::onEpisodesReceived))
        viewModel.isErrorLiveData.observe(requireActivity(), { onErrorReceived() })
        viewModel.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onEpisodesReceived(episodes: List<Episode>) {
        this.episodes = episodes
        episodesListAdapter.updateAdapter(episodes)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_error_title)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                character?.episode?.let {
                    viewModel.loadEpisodes(
                        it
                    )
                }
            }
            .show()
    }

    private fun onLoadingStateReceived(isLoading: Boolean) {
        showSpinner(isLoading)
    }

    private fun showSpinner(isLoading: Boolean) {
        spinner.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}
