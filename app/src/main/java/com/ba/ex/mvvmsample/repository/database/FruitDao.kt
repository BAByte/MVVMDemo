package com.ba.ex.mvvmsample.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ba.ex.mvvmsample.repository.data.Fruit

@Dao
interface FruitDao {

    @Query("SELECT * FROM fruit")
    fun getFruits(): LiveData<List<Fruit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fruits: List<Fruit>): List<Long>

    @Query("SELECT * FROM fruit WHERE id = :id")
    fun getFruit(id: String): Fruit

    @Delete
    suspend fun delete(fruit: Fruit)
}
