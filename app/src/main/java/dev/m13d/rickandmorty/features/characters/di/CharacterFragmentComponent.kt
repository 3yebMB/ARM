package dev.m13d.rickandmorty.features.characters.di

import androidx.lifecycle.ViewModel
import dev.m13d.rickandmorty.di.annotation.ViewModelKey
import dev.m13d.rickandmorty.features.characters.character.CharacterFragment
import dev.m13d.rickandmorty.features.characters.character.CharacterViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [CharacterFragmentModule::class])
interface CharacterFragmentComponent {
    fun inject(characterFragment: CharacterFragment)
}

@Module
interface CharacterFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(CharacterViewModel::class)
    fun bindViewModel(viewModel: CharacterViewModel): ViewModel

}
