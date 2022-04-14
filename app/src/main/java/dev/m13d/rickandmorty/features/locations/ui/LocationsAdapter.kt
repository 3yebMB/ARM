package dev.m13d.rickandmorty.features.locations.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.m13d.rickandmorty.domain.entities.Location
import dev.m13d.rickandmorty.R

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {

    private lateinit var locations: List<Location>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        return LocationsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.location_item, parent, false)
                    as View
        )
    }

    var onClick: (Location) -> Unit = { _ -> }

    override fun getItemCount(): Int = locations.size

    override fun onBindViewHolder(viewHolder: LocationsViewHolder, position: Int) {
        viewHolder.bind(locations[position])
        avoidMultipleClicks(viewHolder.itemView)
        viewHolder.itemView.setOnClickListener {
            onClick(locations[position])
        }
    }

    private fun avoidMultipleClicks(view: View) {
        view.isClickable = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            view.isClickable = true
        }, 1000)
    }

    fun updateAdapter(updatedList: List<Location>) {
        val result = DiffUtil.calculateDiff(LocationsDiffCallback(locations, updatedList))
        this.locations = updatedList.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    fun setData(locations: List<Location>) {
        this.locations = locations
    }

    inner class LocationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.locationName)
        val dimension: TextView = itemView.findViewById(R.id.locationDimension)

        fun bind(location: Location) {
            name.text = "${location.type} ${location.name}"
            dimension.text = location.dimension
        }
    }
}
