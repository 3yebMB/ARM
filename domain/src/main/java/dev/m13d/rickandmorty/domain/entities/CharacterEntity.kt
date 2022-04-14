package dev.m13d.rickandmorty.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "origin")
    val origin: String,
    @ColumnInfo(name = "originUrl")
    val originUrl: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "locationUrl")
    val locationUrl: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["origin"],
            childColumns = ["name"]
        ),
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["location"],
            childColumns = ["name"]
        )
    ],
    tableName = "userLocation"
)
data class UserLocationEntity(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "url")
    val url: String
)
