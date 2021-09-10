package com.daniilmaster.storageanimal.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "animal_table")
data class AnimalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    // имя, год, порода
    val name: String,
    val age: Int,
    val breed: String
) : Parcelable