package dev.m13d.rickandmorty

import android.app.Application
import dev.m13d.rickandmorty.di.Injector

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Injector.initAppComponent(this)
    }

}
