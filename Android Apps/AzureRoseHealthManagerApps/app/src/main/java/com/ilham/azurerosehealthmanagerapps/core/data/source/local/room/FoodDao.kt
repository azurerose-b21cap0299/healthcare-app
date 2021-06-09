package com.ilham.azurerosehealthmanagerapps.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ilham.azurerosehealthmanagerapps.core.data.source.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface FoodDao {

    @Query("SELECT * FROM foodentites ORDER BY foodId DESC")
    fun getFood(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foodentites WHERE name = :name")
    fun getFoodByName(name: String): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: List<FoodEntity>)

}