package dev.m13d.rickandmorty.features.episodes.episode

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dev.m13d.rickandmorty.R.layout.fragment_episode
import dev.m13d.rickandmorty.di.Injector
import dev.m13d.rickandmorty.features.base.BaseFragment
import javax.inject.Inject

private const val EPISODE_ID = "episodeId"

class EpisodeFragment : BaseFragment<EpisodeViewModel>() {

    companion object {
        fun newInstance(episodeId: Int): EpisodeFragment {
            val args = Bundle().apply {
                putInt(EPISODE_ID, episodeId)
            }
            return EpisodeFragment().apply {
                arguments = args
            }
        }
    }

    interface EpisodeCallbacks {
        fun onCharacterItemSelected(characterId: Int)
    }

    private var callbacks: EpisodeCallbacks? = null

    @Inject
    lateinit var characterInteractor: ICharacterInteractor

    @Inject
    lateinit var episodeInteractor: IEpisodeInteractor

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    private var characters: List<Character> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var charactersListAdapter: CharactersListAdapter
    private var episode: Episode? = null
    private lateinit var spinner: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as EpisodeCallbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Injector.episodeFragmentComponent.inject(this)
        injectViewModel()
        return inflater.inflate(fragment_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.progress_bar)
        if (episode == null) {
            val episodeId = arguments?.getInt(EPISODE_ID)
            episode = viewModel.loadEpisode(episodeId!!)
            episode?.characters?.let { viewModel.loadCharacters(it) }
        }
        initViews(view)
        observeLiveData()
    }

    private fun initViews(view: View) {
        view.findViewById<TextView>(R.id.episode).text = episode?.episode
        view.findViewById<TextView>(R.id.name).text = episode?.name
        view.findViewById<TextView>(R.id.date).text = episode?.date

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
        charactersListAdapter = CharactersListAdapter()
        charactersListAdapter.onClick = { character ->
            callbacks?.onCharacterItemSelected(character.id)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = charactersListAdapter
            charactersListAdapter.setData(characters)
        }
    }

    private fun observeLiveData() {
        viewModel.charactersLiveData.observe(requireActivity(), Observer(::onCharactersReceived))
        viewModel.isErrorLiveData.observe(requireActivity(), { onErrorReceived() })
        viewModel.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onCharactersReceived(characters: List<Character>) {
        this.characters = characters
        charactersListAdapter.updateAdapter(characters)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_error_title)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                episode?.characters?.let {
                    viewModel.loadCharacters(
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
