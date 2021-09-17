package com.daniilmaster.storageanimal.repository

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.db.AppDao
import com.daniilmaster.storageanimal.db.AppHelperDatabase

class AppRepository(
    private val appDao: AppDao,
    private var dbHelper: AppHelperDatabase,
    private val application: Application,
    private val pref: SharedPreferences
) {
    private val isRoom get() = pref.getBoolean("switch_room_or_cursor", true)

    suspend fun addAnimal(animalEntity: AnimalEntity) {
        if (isRoom)
            appDao.addAnimal(animalEntity)
        else // cursor work
            dbHelper.addAnimal(animalEntity)
    }

    suspend fun updateAnimal(animalEntity: AnimalEntity) {

        if (isRoom)
            appDao.updateAnimal(animalEntity)
        else // cursor work
            dbHelper.updateAnimal(animalEntity)
    }

    suspend fun deleteAnimal(animalEntity: AnimalEntity) {
        if (isRoom)
            appDao.deleteAnimal(animalEntity)
        else // cursor work (with dialog)
            dbHelper.deleteAnimal(animalEntity)
    }

    suspend fun deleteAllAnimals() {
        if (isRoom)
            appDao.deleteAllAnimals()
        else // cursor work (with dialog)
            dbHelper.deleteAllAnimals()
    }

    fun allAnimals(): LiveData<List<AnimalEntity>> {
        val selectedFilter = pref.getString("list_preference_filter", "id").toString()
        val animals: LiveData<List<AnimalEntity>>

        if (isRoom) {
            animals = when (selectedFilter) {
                "id" -> appDao.filterId()
                "name" -> appDao.filterName()
                "age" -> appDao.filterAge()
                "breed" -> appDao.filterBreed()
                else -> appDao.filterId()
            }
        } else { // cursor work
            dbHelper = AppHelperDatabase(application)
            animals = dbHelper.filterSelect(selectedFilter)
        }

        return animals
    }
}