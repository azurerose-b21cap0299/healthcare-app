package com.ilham.azurerosehealthmanagerapps.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foodentites")
data class FoodEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "foodId")
    var foodId: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "calorie")
    var kcalorie: Double,

    @ColumnInfo(name = "protein")
    var protein: Double,

    @ColumnInfo(name = "fats")
    var fats: Double,

    @ColumnInfo(name = "carbs")
    var carbs: Double,

    @ColumnInfo(name = "fiber")
    var fiber: Double


)
