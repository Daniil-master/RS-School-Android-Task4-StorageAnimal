package com.daniilmaster.storageanimal.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface AppDao {
    // DESC -  в порядке убывания
    // ASC - в порядке возрастания
    @Query("SELECT * FROM animal_table ORDER BY :filterName ASC")
    fun getAllAnimals(filterName: String): LiveData<List<AnimalEntity>>

    @Query("SELECT * FROM animal_table ORDER BY id ASC")
    fun filterId(): LiveData<List<AnimalEntity>>

    @Query("SELECT * FROM animal_table ORDER BY name ASC")
    fun filterName(): LiveData<List<AnimalEntity>>

    @Query("SELECT * FROM animal_table ORDER BY age ASC")
    fun filterAge(): LiveData<List<AnimalEntity>>

    @Query("SELECT * FROM animal_table ORDER BY breed ASC")
    fun filterBreed(): LiveData<List<AnimalEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAnimal(animalEntity: AnimalEntity)

    @Update
    suspend fun updateAnimal(animalEntity: AnimalEntity)

    @Delete
    suspend fun deleteAnimal(animalEntity: AnimalEntity)

    @Query("DELETE FROM animal_table")
    suspend fun deleteAllAnimals()


//    // Поисковой запрос (подставка значений)
//    @Query("SELECT * FROM animal_table WHERE name LIKE :searchQuery OR age LIKE :searchQuery")
//    fun searchDatabase(searchQuery: String): LiveData<List<AnimalEntity>>

}