package dev.m13d.rickandmorty.features.episodes.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.m13d.rickandmorty.domain.entities.Episode
import dev.m13d.rickandmorty.domain.entities.EpisodesFilter
import dev.m13d.rickandmorty.domain.interactors.IEpisodeInteractor
import dev.m13d.rickandmorty.R
import dev.m13d.rickandmorty.R.layout.fragment_episodes
import dev.m13d.rickandmorty.di.Injector
import dev.m13d.rickandmorty.features.base.BaseFragment
import dev.m13d.rickandmorty.features.episodes.viewModel.EpisodesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dev.m13d.rickandmorty.features.episodes.ui.EpisodesDialogFragment
import javax.inject.Inject

class EpisodesFragment :
    BaseFragment<EpisodesViewModel>(),
    EpisodesDialogFragment.DialogCallback {

    companion object {
        fun newInstance() = EpisodesFragment()
        var page = 1
        var pages = 1
        var filter = EpisodesFilter()
    }

    interface EpisodesCallbacks {
        fun onEpisodeItemSelected(episodeId: Int)
    }

    private var callbacks: EpisodesCallbacks? = null

    @Inject
    lateinit var episodesInteractor: IEpisodeInteractor

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    private lateinit var episodesAdapter: EpisodesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var search: EditText
    private lateinit var spinner: ProgressBar
    private lateinit var fab: FloatingActionButton

    private var episodes = emptyList<Episode>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        callbacks = context as EpisodesCallbacks?
        Injector.episodesFragmentComponent.inject(this)
        injectViewModel()
        return inflater.inflate(fragment_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.progress_bar)
        search = requireView().findViewById(R.id.search)
        search.setText(filter.name)
        search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    filter.name = search.text.toString()
                    page = 1
                    viewModel.reloadEpisodes(filter)
                    return true
                }
                return false
            }
        })
        fab = view.findViewById(R.id.filter_button)
        fab.setOnClickListener {
            val dialog = EpisodesDialogFragment()
            dialog.setTargetFragment(this, 1)
            dialog.show(parentFragmentManager, "dialog")
        }
        view.findViewById<TextInputLayout>(R.id.input_layout).setEndIconOnClickListener {
            filter.name = search.text.toString()
            page = 1
            viewModel.reloadEpisodes(filter)
        }
        initRecyclerView()
        observeLiveData()
        if (episodes.isNullOrEmpty()) {
            page = 1
            viewModel.loadEpisodes(page, filter)
        }
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            viewModel.reloadEpisodes(filter)
            swipe.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recycler_view)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.divider
            )!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        episodesAdapter = EpisodesAdapter()
        episodesAdapter.onClick = { episode ->
            callbacks?.onEpisodeItemSelected(episode.id)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = episodesAdapter
            episodesAdapter.setData(episodes)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModel.loadEpisodes(page, filter)
                }
                if (dy > 0)
                    fab.hide()
                else if (dy < 0)
                    fab.show()
            }
        })
    }

    private fun observeLiveData() {
        viewModel.episodesLiveData.observe(requireActivity(), Observer(::onEpisodesReceived))
        viewModel.isErrorLiveData.observe(requireActivity(), { onErrorReceived() })
        viewModel.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onEpisodesReceived(episodes: List<Episode>) {
        this.episodes = episodes
        episodesAdapter.updateAdapter(episodes)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_no_data)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                viewModel.reloadEpisodes(filter)
            }
            .show()
        filter = EpisodesFilter()
    }

    private fun onLoadingStateReceived(isLoading: Boolean) {
        spinner.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onFilter() {
        viewModel.reloadEpisodes(filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        callbacks = null
        page = 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("recycler", recyclerView.layoutManager?.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val savedRecyclerLayoutState: Parcelable =
                savedInstanceState.getParcelable("recycler")!!
            recyclerView.layoutManager?.onRestoreInstanceState(savedRecyclerLayoutState)
        }
    }
}
