package com.codeChallenge.olympicChannel.di.modules

import androidx.lifecycle.ViewModel
import com.codeChallenge.olympicChannel.di.viewModelsInjections.ViewModelKey
import com.codeChallenge.olympicChannel.viewModel.activities.main.MainActivityViewModel
import com.codeChallenge.olympicChannel.viewModel.fragments.athleteDetail.FragmentAthleteDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMyViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentAthleteDetailViewModel::class)
    internal abstract fun bindMyViewModelAthleteDetail(viewModel: FragmentAthleteDetailViewModel): ViewModel


}