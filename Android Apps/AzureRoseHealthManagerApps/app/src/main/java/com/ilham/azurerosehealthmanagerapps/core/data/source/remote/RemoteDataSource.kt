package com.ilham.azurerosehealthmanagerapps.core.data.source.remote

import android.util.Log
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.network.ApiService
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.FoodDetailResponse
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.FoodResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getSearchFood(name: String): Flow<ApiResponse<List<FoodResponse>>> {
        return flow {
            try {
                val response = apiService.searchFood(name)
                val dataArray = response.parsed
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.parsed))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchHintsFood(name: String): Flow<ApiResponse<List<FoodResponse>>> {
        return flow {
            try {
                val response = apiService.searchFood(name)
                val dataArray = response.hint
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.hint))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }


}
