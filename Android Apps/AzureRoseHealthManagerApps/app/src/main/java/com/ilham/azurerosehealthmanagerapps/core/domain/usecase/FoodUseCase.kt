package com.ilham.azurerosehealthmanagerapps.core.domain.usecase

import com.ilham.azurerosehealthmanagerapps.core.data.Resource
import com.ilham.azurerosehealthmanagerapps.core.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodUseCase {
    fun getSearchFood(name: String): Flow<Resource<List<Food>>>


}