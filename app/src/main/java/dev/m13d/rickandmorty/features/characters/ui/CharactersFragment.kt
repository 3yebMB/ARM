package dev.m13d.rickandmorty.features.characters.ui

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.domain.entities.CharactersFilter
import dev.m13d.rickandmorty.domain.interactors.ICharacterInteractor
import dev.m13d.rickandmorty.R
import dev.m13d.rickandmorty.R.layout.fragment_characters
import dev.m13d.rickandmorty.di.Injector
import dev.m13d.rickandmorty.features.base.BaseFragment
import dev.m13d.rickandmorty.features.characters.viewModel.CharactersViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class CharactersFragment :
    BaseFragment<CharactersViewModel>(),
    CharactersDialogFragment.DialogCallback {

    companion object {
        fun newInstance() = CharactersFragment()
        var page = 1
        var pages = 1
        var filter = CharactersFilter()
    }

    interface CharactersCallbacks {
        fun onCharacterItemSelected(characterId: Int)
    }

    private var callbacks: CharactersCallbacks? = null

    @Inject
    lateinit var charactersInteractor: ICharacterInteractor

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    private lateinit var charactersAdapter: CharactersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var search: EditText
    private lateinit var spinner: ProgressBar
    private lateinit var fab: FloatingActionButton

    private var characters = emptyList<Character>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        callbacks = context as CharactersCallbacks?
        Injector.charactersFragmentComponent.inject(this)
        injectViewModel()
        return inflater.inflate(fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.progress_bar)
        search = view.findViewById(R.id.search)
        search.setText(filter.name)
        search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    filter.name = search.text.toString()
                    page = 1
                    viewModel.reloadCharacters(filter)
                    return true
                }
                return false
            }
        })
        fab = view.findViewById(R.id.filter_button)
        fab.setOnClickListener {
            val dialog = CharactersDialogFragment()
            dialog.setTargetFragment(this, 1)
            dialog.show(parentFragmentManager, "dialog")
        }
        view.findViewById<TextInputLayout>(R.id.input_layout).setEndIconOnClickListener {
            filter.name = search.text.toString()
            page = 1
            viewModel.reloadCharacters(filter)
        }
        initRecyclerView()
        observeLiveData()
        if (characters.isNullOrEmpty()) {
            page = 1
            viewModel.loadCharacters(page, filter)
        }
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            viewModel.reloadCharacters(filter)
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
//        recyclerView.addItemDecoration(dividerItemDecoration)
        charactersAdapter = CharactersAdapter()
        charactersAdapter.onClick = { character ->
            callbacks?.onCharacterItemSelected(character.id)
        }
        recyclerView.apply {
//            layoutManager = LinearLayoutManager(requireContext())
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = charactersAdapter
            charactersAdapter.setData(characters)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModel.loadCharacters(page, filter)
                }
                if (dy > 0)
                    fab.hide()
                else if (dy < 0)
                    fab.show()
            }
        })

    }

    private fun observeLiveData() {
        viewModel.charactersLiveData.observe(requireActivity(), Observer(::onCharactersReceived))
        viewModel.isErrorLiveData.observe(requireActivity(), { onErrorReceived() })
        viewModel.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onCharactersReceived(characters: List<Character>) {
        this.characters = characters
        charactersAdapter.updateAdapter(characters)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_no_data)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                viewModel.reloadCharacters(filter)
            }
            .show()
        filter = CharactersFilter()
    }

    private fun onLoadingStateReceived(isLoading: Boolean) {
        spinner.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onFilter() {
        viewModel.reloadCharacters(filter)
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
