package dev.m13d.rickandmorty.di.modules

import dev.m13d.rickandmorty.data.network.RickAndMortyApi
import dev.m13d.rickandmorty.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RestModule {

    @Singleton
    @Provides
    fun provideRickAndMortyApi(retrofit: Retrofit): RickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(RICK_AND_MORTY_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideLogger() = HttpLoggingInterceptor()
        .setLevel(
        if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
        )

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().create()

    companion object {
        private const val RICK_AND_MORTY_URL = "https://rickandmortyapi.com/api/"
    }

}
