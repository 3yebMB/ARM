package dev.m13d.rickandmorty.domain.mapper

interface BaseMapper<in A, out B> {

    fun map(type: A?): B
}
