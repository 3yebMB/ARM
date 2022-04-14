package dev.m13d.rickandmorty.features.characters.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.m13d.rickandmorty.domain.entities.Character
import dev.m13d.rickandmorty.R
import com.bumptech.glide.Glide

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    private lateinit var characters: List<Character>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.character_item, parent, false)
                    as View
        )
    }

    var onClick: (Character) -> Unit = { _ -> }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(viewHolder: CharactersViewHolder, position: Int) {
        viewHolder.bind(characters[position])
        avoidMultipleClicks(viewHolder.itemView)
        viewHolder.itemView.setOnClickListener {
            onClick(characters[position])
        }
    }

    private fun avoidMultipleClicks(view: View) {
        view.isClickable = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            view.isClickable = true
        }, 1000)
    }

    fun updateAdapter(updatedList: List<Character>) {
        val result = DiffUtil.calculateDiff(CharactersDiffCallback(characters, updatedList))
        this.characters = updatedList.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    fun setData(characters: List<Character>) {
        this.characters = characters
    }

    inner class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.characterName)
        private val species: TextView = itemView.findViewById(R.id.characterSpecies)
        private val status: TextView = itemView.findViewById(R.id.characterStatus)
        private val gender: TextView = itemView.findViewById(R.id.characterGender)
        private val image: ImageView = itemView.findViewById(R.id.characterImage)

        fun bind(character: Character) {
            name.text = character.name
            species.text = character.species
            status.text = character.status
            gender.text = character.gender
            Glide
                .with(itemView.context)
                .load(character.image)
                .into(image)
        }
    }

}
