package com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class FoodDetailResponse(
    @field:SerializedName("foodId")
    var foodId: String,

    @field:SerializedName("label")
    var label: String,

    @field:SerializedName("nutrients")
    var nutrients:  NutrientsResponse,

    @field:SerializedName("image")
    var image: String

) : Parcelable


