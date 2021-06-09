package com.ilham.azurerosehealthmanagerapps.core.domain.repository

import com.ilham.azurerosehealthmanagerapps.core.data.Resource
import com.ilham.azurerosehealthmanagerapps.core.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface IFoodRepository {

    fun getSearchFood(name: String): Flow<Resource<List<Food>>>


}