package dev.m13d.rickandmorty.features.locations.ui

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
import dev.m13d.rickandmorty.domain.entities.Location
import dev.m13d.rickandmorty.domain.entities.LocationsFilter
import dev.m13d.rickandmorty.domain.interactors.ILocationInteractor
import dev.m13d.rickandmorty.R
import dev.m13d.rickandmorty.R.layout.fragment_locations
import dev.m13d.rickandmorty.di.Injector
import dev.m13d.rickandmorty.features.base.BaseFragment
import dev.m13d.rickandmorty.features.locations.viewModel.LocationsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class LocationsFragment :
    BaseFragment<LocationsViewModel>(),
    LocationsDialogFragment.DialogCallback {

    companion object {
        fun newInstance() = LocationsFragment()
        var page = 1
        var pages = 1
        var filter = LocationsFilter()
    }

    interface LocationsCallbacks {
        fun onLocationItemSelected(locationId: Int)
    }

    private var callbacks: LocationsCallbacks? = null

    @Inject
    lateinit var locationsInteractor: ILocationInteractor

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    private lateinit var locationsAdapter: LocationsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var search: EditText
    private lateinit var spinner: ProgressBar
    private lateinit var fab: FloatingActionButton

    private var locations = emptyList<Location>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        callbacks = context as LocationsCallbacks?
        Injector.locationsFragmentComponent.inject(this)
        injectViewModel()
        return inflater.inflate(fragment_locations, container, false)
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
                    viewModel.reloadLocations(filter)
                    return true
                }
                return false
            }
        })
        fab = view.findViewById(R.id.filter_button)
        fab.setOnClickListener {
            val dialog = LocationsDialogFragment()
            dialog.setTargetFragment(this, 1)
            dialog.show(parentFragmentManager, "dialog")
        }
        view.findViewById<TextInputLayout>(R.id.input_layout).setEndIconOnClickListener {
            filter.name = search.text.toString()
            page = 1
            viewModel.reloadLocations(filter)
        }
        initRecyclerView()
        observeLiveData()
        if (locations.isNullOrEmpty()) {
            page = 1
            viewModel.loadLocations(page, filter)
        }
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            viewModel.reloadLocations(filter)
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
        locationsAdapter = LocationsAdapter()
        locationsAdapter.onClick = { location ->
            callbacks?.onLocationItemSelected(location.id)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationsAdapter
            locationsAdapter.setData(locations)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModel.loadLocations(page, filter)
                }
                if (dy > 0)
                    fab.hide()
                else if (dy < 0)
                    fab.show()
            }
        })
    }

    private fun observeLiveData() {
        viewModel.locationsLiveData.observe(requireActivity(), Observer(::onLocationsReceived))
        viewModel.isErrorLiveData.observe(requireActivity(), { onErrorReceived() })
        viewModel.isLoadingLiveData.observe(requireActivity(), Observer(::onLoadingStateReceived))
    }

    private fun onLocationsReceived(locations: List<Location>) {
        this.locations = locations
        locationsAdapter.updateAdapter(locations)
    }

    private fun onErrorReceived() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_connection_no_data)
            .setCancelable(false)
            .setNegativeButton(R.string.network_connection_error_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.network_connection_error_action) { _, _ ->
                viewModel.reloadLocations(filter)
            }
            .show()
        filter = LocationsFilter()
    }

    private fun onLoadingStateReceived(isLoading: Boolean) {
        spinner.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onFilter() {
        viewModel.reloadLocations(filter)
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
