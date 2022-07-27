package com.daniilmaster.storageanimal.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = "animal_table")
data class AnimalEntity(
    @PrimaryKey(autoGenerate = true)
   @NotNull val id: Int = 0,
    // имя, год, порода
    @NotNull  val name: String = "",
    @NotNull  val age: Int = 1,
    @NotNull  val breed: String = ""
) : Parcelable