package com.ilham.azurerosehealthmanagerapps.core.data

import androidx.lifecycle.map
import com.ilham.azurerosehealthmanagerapps.core.data.source.local.LocalDataSource
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.ApiResponse
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.RemoteDataSource
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.FoodDetailResponse
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.FoodResponse
import com.ilham.azurerosehealthmanagerapps.core.domain.model.Food
import com.ilham.azurerosehealthmanagerapps.core.domain.repository.IFoodRepository
import com.ilham.azurerosehealthmanagerapps.core.utils.AppExecutors
import com.ilham.azurerosehealthmanagerapps.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): IFoodRepository {
    override fun getSearchFood(name: String): Flow<Resource<List<Food>>> =
        object : NetworkBoundResource<List<Food>,List<FoodResponse>>() {
            override fun loadFromDB(): Flow<List<Food>> {
                return localDataSource.getFood(name).map { DataMapper.mapFoodEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Food>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<FoodResponse>>> =
                remoteDataSource.getSearchFood(name)

            override suspend fun saveCallResult(data: List<FoodResponse>) {
                val foodList = DataMapper.mapFoodResponseToEntities(data)
                localDataSource.insertFood(foodList)
            }
        }.asFlow()



}