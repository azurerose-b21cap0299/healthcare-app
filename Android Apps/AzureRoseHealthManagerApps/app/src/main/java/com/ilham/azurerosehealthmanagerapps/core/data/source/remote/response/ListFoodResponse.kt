package com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListFoodResponse(
    @field:SerializedName("text")
    val name: String,

    @field:SerializedName("parsed")
    val parsed: List<FoodResponse>,

    @field:SerializedName("hints")
    val hint: List<FoodResponse>

)