package com.ilham.azurerosehealthmanagerapps.core.data.source.remote.network

import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.ListFoodResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val APP_ID = "5572abb7"
        const val APP_KEY = "e4a24a1243c55cdbaf90fb05f862c31f"
    }

    @GET("food-database/v2/parser?")
    suspend fun searchFood(
        @Query("ingr") ingr: String,
        @Query("app_id") app_id: String = APP_ID,
        @Query("app_key") app_key: String = APP_KEY
    ) : ListFoodResponse



}
//https://api.edamam.com/api/food-database/v2/parser?ingr=red%20apple&app_id=5572abb7&app_key=e4a24a1243c55cdbaf90fb05f862c31f
