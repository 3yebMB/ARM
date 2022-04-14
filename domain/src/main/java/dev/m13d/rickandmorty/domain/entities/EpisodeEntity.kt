package dev.m13d.rickandmorty.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes")
class EpisodeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "episode")
    val episode: String,
    @ColumnInfo(name = "url")
    val url: String
)
