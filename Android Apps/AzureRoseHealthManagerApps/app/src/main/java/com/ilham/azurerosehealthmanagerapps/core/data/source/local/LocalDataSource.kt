package com.ilham.azurerosehealthmanagerapps.core.data.source.local

import com.ilham.azurerosehealthmanagerapps.core.data.source.local.entity.FoodEntity
import com.ilham.azurerosehealthmanagerapps.core.data.source.local.room.FoodDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val mFoodDao: FoodDao) {

    fun getAllFood(): Flow<List<FoodEntity>> = mFoodDao.getFood()

    fun getFood(name: String) : Flow<List<FoodEntity>> = mFoodDao.getFoodByName(name)

    suspend fun insertFood(food: List<FoodEntity>) = mFoodDao.insert(food)

}