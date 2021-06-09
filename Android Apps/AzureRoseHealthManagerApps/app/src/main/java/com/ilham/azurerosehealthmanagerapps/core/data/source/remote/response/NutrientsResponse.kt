package com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class NutrientsResponse(
    @field:SerializedName("ENERC_KCAL")
    var calorie: Double,

    @field:SerializedName("PROCNT")
    var protein: Double,

    @field:SerializedName("FAT")
    var fat: Double,

    @field:SerializedName("CHOCDF")
    var carbs: Double,

    @field:SerializedName("FIBTG")
    var fiber: Double
    ): Parcelable
