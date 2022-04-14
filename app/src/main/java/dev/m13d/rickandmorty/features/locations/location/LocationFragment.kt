package dev.m13d.rickandmorty.features.locations.location

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
import dev.m13d.rickandmorty.domain.entities.Location
import dev.m13d.rickandmorty.domain.interactors.ICharacterInteractor
import dev.m13d.rickandmorty.domain.interactors.ILocationInteractor
import dev.m13d.rickandmorty.R
import dev.m13d.rickandmorty.R.layout.fragment_location
import dev.m13d.rickandmorty.di.Injector
import dev.m13d.rickandmorty.features.base.BaseFragment
import javax.inject.Inject

private const val LOCATION_ID = "locationId"

class LocationFragment : BaseFragment<LocationViewModel>() {

    companion object {
        fun newInstance(locationId: Int): LocationFragment {
            val args = Bundle().apply {
                putInt(LOCATION_ID, locationId)
            }
            return LocationFragment().apply {
                arguments = args
            }
        }
    }

    interface LocationCallbacks {
        fun onCharacterItemSelected(characterId: Int)
    }

    private var callbacks: LocationCallbacks? = null

    @Inject
    lateinit var charactersInteractor: ICharacterInteractor

    @Inject
    lateinit var locationsInteractor: ILocationInteractor

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    private var characters: List<Character> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var charactersListAdapter: CharactersListAdapter
    private var location: Location? = null
    private lateinit var spinner: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as LocationCallbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Injector.locationFragmentComponent.inject(this)
        injectViewModel()
        return inflater.inflate(fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.progress_bar)
        if (location == null) {
            val locationId = arguments?.getInt(LOCATION_ID)
            location = viewModel.loadLocation(locationId!!)
            location?.characters?.let { viewModel.loadCharacters(it) }
        }
        initViews(view)
        observeLiveData()
    }

    private fun initViews(view: View) {
        view.findViewById<TextView>(R.id.location).text = "${location?.type} ${location?.name}"
        view.findViewById<TextView>(R.id.dimension).text = location?.dimension

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
                location?.characters?.let {
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
