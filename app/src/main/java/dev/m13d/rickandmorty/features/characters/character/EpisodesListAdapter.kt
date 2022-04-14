package dev.m13d.rickandmorty.features.characters.character

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.m13d.rickandmorty.domain.entities.Episode
import dev.m13d.rickandmorty.R
import dev.m13d.rickandmorty.features.episodes.ui.EpisodesDiffCallback

class EpisodesListAdapter : RecyclerView.Adapter<EpisodesListAdapter.EpisodesViewHolder>() {

    private lateinit var episodes: List<Episode>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item, parent, false)
                    as View
        )
    }

    var onClick: (Episode) -> Unit = { _ -> }

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(viewHolder: EpisodesViewHolder, position: Int) {
        viewHolder.bind(episodes[position])
        avoidMultipleClicks(viewHolder.itemView)
        viewHolder.itemView.setOnClickListener {
            onClick(episodes[position])
        }
    }

    private fun avoidMultipleClicks(view: View) {
        view.isClickable = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            view.isClickable = true
        }, 1000)
    }

    fun updateAdapter(updatedList: List<Episode>) {
        val result = DiffUtil.calculateDiff(EpisodesDiffCallback(episodes, updatedList))
        this.episodes = updatedList.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    fun setData(episodes: List<Episode>) {
        this.episodes = episodes
    }

    inner class EpisodesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.text)

        fun bind(episode: Episode) {
            name.text = "${episode.episode}: ${episode.name}"
        }
    }
}
