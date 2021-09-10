package com.daniilmaster.storageanimal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.daniilmaster.storageanimal.db.AnimalEntity
import com.daniilmaster.storageanimal.db.AppDatabase
import com.daniilmaster.storageanimal.db.AppHelperDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    var animals: LiveData<List<AnimalEntity>>
    private val repository: AppRepository
    private val dbHelper = AppHelperDatabase(application)

    init {
        val appDao = AppDatabase.getDatabase(application).getDao()
        repository = AppRepository(appDao, dbHelper, application)
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