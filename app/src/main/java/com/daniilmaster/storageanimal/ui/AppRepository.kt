package com.daniilmaster.storageanimal.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.db.AppDao
import com.daniilmaster.storageanimal.db.AppHelperDatabase

class AppRepository(
    private val appDao: AppDao,
    private var dbHelper: AppHelperDatabase,
    private val application: Application
) {
    private val pref = PreferenceManager.getDefaultSharedPreferences(application)
    private var isRoom = pref.getBoolean("switch_room_or_cursor", true)

    suspend fun addAnimal(animalEntity: AnimalEntity) {
        getIsRoom()

        if (isRoom)
            appDao.addAnimal(animalEntity)
        else // cursor work
            dbHelper.addAnimal(animalEntity)
    }


    suspend fun updateAnimal(animalEntity: AnimalEntity) {
        getIsRoom()

        if (isRoom)
            appDao.updateAnimal(animalEntity)
        else // cursor work
            dbHelper.updateAnimal(animalEntity)
    }

    suspend fun deleteAnimal(animalEntity: AnimalEntity) {
        getIsRoom()

        if (isRoom)
            appDao.deleteAnimal(animalEntity)
        else // cursor work (with dialog)
            dbHelper.deleteAnimal(animalEntity)

    }

    suspend fun deleteAllAnimals() {
        getIsRoom()

        if (isRoom)
            appDao.deleteAllAnimals()
        else // cursor work (with dialog)
            dbHelper.deleteAllAnimals()

    }

    fun allAnimals(): LiveData<List<AnimalEntity>> {
        getIsRoom()
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


    private fun getIsRoom() {
        isRoom = pref.getBoolean("switch_room_or_cursor", true)
    }


}