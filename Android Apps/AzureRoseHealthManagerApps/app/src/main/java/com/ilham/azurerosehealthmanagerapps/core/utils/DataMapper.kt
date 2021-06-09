package com.ilham.azurerosehealthmanagerapps.core.utils

import com.ilham.azurerosehealthmanagerapps.core.data.source.local.entity.FoodEntity
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.FoodDetailResponse
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.FoodResponse
import com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response.NutrientsResponse
import com.ilham.azurerosehealthmanagerapps.core.domain.model.Food

object DataMapper {
    fun mapFoodResponseToEntities(input: List<FoodResponse>): List<FoodEntity> {
        val foodList = ArrayList<FoodEntity>()
        input.map {
            val food = FoodEntity(
                foodId = it.food.foodId,
                name = it.food.label,
                image = it.food.image,
                kcalorie = it.food.nutrients.calorie,
                protein = it.food.nutrients.protein,
                fats = it.food.nutrients.fat,
                carbs = it.food.nutrients.carbs,
                fiber = it.food.nutrients.fiber
            )
            foodList.add(food)
        }
        return foodList
    }

    fun mapFoodEntitiesToDomain(input: List<FoodEntity>): List<Food> =
        input.map {
            Food(
                foodId = it.foodId,
                label = it.name,
                image = it.image,
                kcalorie = it.kcalorie,
                protein = it.protein,
                fats = it.fats,
                carbs = it.carbs,
                fiber = it.fiber
            )
        }

    fun mapFoodDomainToEntity(input: Food) = FoodEntity(
        foodId = input.foodId,
        name = input.label,
        image = input.image,
        kcalorie = input.kcalorie,
        protein = input.protein,
        fats = input.fats,
        carbs = input.carbs,
        fiber = input.fiber
    )
}