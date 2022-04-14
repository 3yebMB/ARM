package dev.m13d.rickandmorty.features.base

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    protected open val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            showError(coroutineContext, throwable)
        }

    override val coroutineContext =
        Dispatchers.Main + CoroutineName("${this::class}") + coroutineExceptionHandler

    protected open fun showError(coroutineContext: CoroutineContext, throwable: Throwable) {
        Log.e("AAA", "$throwable where $coroutineContext")
    }

}
