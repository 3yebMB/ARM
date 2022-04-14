package dev.m13d.rickandmorty.features.characters.ui

import androidx.recyclerview.widget.DiffUtil
import dev.m13d.rickandmorty.domain.entities.Character

class CharactersDiffCallback(
    private val oldList: List<Character>,
    private val newList: List<Character>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}
