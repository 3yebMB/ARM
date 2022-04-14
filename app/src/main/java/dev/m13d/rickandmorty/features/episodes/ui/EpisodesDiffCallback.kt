package dev.m13d.rickandmorty.features.episodes.ui

import androidx.recyclerview.widget.DiffUtil
import dev.m13d.rickandmorty.domain.entities.Episode

class EpisodesDiffCallback(
    private val oldList: List<Episode>,
    private val newList: List<Episode>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}
