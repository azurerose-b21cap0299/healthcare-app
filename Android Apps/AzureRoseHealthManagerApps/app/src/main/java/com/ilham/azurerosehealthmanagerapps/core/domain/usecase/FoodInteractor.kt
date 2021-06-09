package com.ilham.azurerosehealthmanagerapps.core.domain.usecase

import com.ilham.azurerosehealthmanagerapps.core.data.Resource
import com.ilham.azurerosehealthmanagerapps.core.domain.model.Food
import com.ilham.azurerosehealthmanagerapps.core.domain.repository.IFoodRepository
import kotlinx.coroutines.flow.Flow

class FoodInteractor(private val foodRepository: IFoodRepository): FoodUseCase {
    override fun getSearchFood(name: String) = foodRepository.getSearchFood(name)

}