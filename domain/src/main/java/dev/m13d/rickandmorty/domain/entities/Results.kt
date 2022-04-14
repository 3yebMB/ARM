package dev.m13d.rickandmorty.domain.entities

import com.google.gson.annotations.SerializedName

data class Characters(
    @field:SerializedName("results")
    val results: List<Character>,
    @field:SerializedName("info")
    val info: Info
)

data class Locations(
    @field:SerializedName("results")
    val results: List<Location>,
    @field:SerializedName("info")
    val info: Info
)

data class Episodes(
    @field:SerializedName("results")
    val results: List<Episode>,
    @field:SerializedName("info")
    val info: Info
)

data class Info(
    @field:SerializedName("pages")
    val pages: Int
)
