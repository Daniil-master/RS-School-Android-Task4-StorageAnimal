package com.daniilmaster.storageanimal.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.db.AppDatabase
import com.daniilmaster.storageanimal.db.AppHelperDatabase
import com.daniilmaster.storageanimal.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val appDao by lazy { AppDatabase.getDatabase(application).getDao() }
    private val dbHelper: AppHelperDatabase by lazy { AppHelperDatabase(application) }
    private val pref: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            application
        )
    }
    private val repository: AppRepository by lazy {
        AppRepository(
            appDao,
            dbHelper,
            application,
            pref
        )
    }

    var animals: LiveData<List<AnimalEntity>>

    init {
        animals = repository.allAnimals()
    }

    fun allAnimals() {
        animals = repository.allAnimals()
    }

    fun addAnimal(animalEntity: AnimalEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAnimal(animalEntity)
        }
    }

    fun updateAnimal(animalEntity: AnimalEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAnimal(animalEntity)
        }
    }

    fun deleteAnimal(animalEntity: AnimalEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAnimal(animalEntity)
        }
    }

    fun deleteAllAnimals() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAnimals()
        }
    }

}