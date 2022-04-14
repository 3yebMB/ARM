package dev.m13d.rickandmorty.domain.entities

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("origin")
    val origin: CharacterLocation,
    @SerializedName("location")
    val location: CharacterLocation,
    @SerializedName("episode")
    val episode: List<String>?
)

data class CharacterLocation(
    val name: String,
    val url: String
)
