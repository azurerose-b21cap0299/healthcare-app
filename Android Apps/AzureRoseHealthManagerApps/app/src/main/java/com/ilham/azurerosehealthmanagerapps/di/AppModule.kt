package com.ilham.azurerosehealthmanagerapps.di

import com.ilham.azurerosehealthmanagerapps.core.domain.usecase.FoodInteractor
import com.ilham.azurerosehealthmanagerapps.core.domain.usecase.FoodUseCase
import com.ilham.azurerosehealthmanagerapps.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<FoodUseCase> { FoodInteractor(get()) }
}
val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
}
