package com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @field:SerializedName("food")
    val food: FoodDetailResponse
)
