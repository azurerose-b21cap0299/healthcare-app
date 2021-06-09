package com.ilham.azurerosehealthmanagerapps.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ilham.azurerosehealthmanagerapps.core.data.Resource
import com.ilham.azurerosehealthmanagerapps.core.domain.model.Food
import com.ilham.azurerosehealthmanagerapps.core.domain.usecase.FoodUseCase
import kotlinx.coroutines.flow.Flow

class SearchViewModel(private val foodUseCase: FoodUseCase) : ViewModel() {

   fun searchFood(name: String) : LiveData<Resource<List<Food>>> = foodUseCase.getSearchFood(name).asLiveData()


}