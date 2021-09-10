package com.daniilmaster.storageanimal.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppHelperDatabase(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    // Имена (константы)
    companion object {
        private const val DATABASE_NAME = "app_database"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "animal_table"

        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_AGE = "age"
        private const val KEY_BREED = "breed"
    }

    // УПРАВЛЕНИЕ ТАБЛИЦАМИ (встроенные)
    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = ("CREATE TABLE "
                + TABLE_NAME
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_AGE + " INTEGER, "
                + KEY_BREED + " TEXT"
                + ")")
        db?.execSQL(createQuery)
    }

    // Пересоздание
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // ОБРАБОТКА ДАННЫХ ТАБЛИЦЫ animal_table

    // Добавление данных (Insert)
    fun addAnimal(animal: AnimalEntity): Long {
        val db = writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, animal.name)
        contentValues.put(KEY_AGE, animal.age)
        contentValues.put(KEY_BREED, animal.breed)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success // результат запроса (больше -1 успех)
    }

    // Обновление записи (Update)
    fun updateAnimal(animal: AnimalEntity): Int {
        val db = writableDatabase // на запись

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, animal.name)
        contentValues.put(KEY_AGE, animal.age)
        contentValues.put(KEY_BREED, animal.breed)

        val success = db.update(TABLE_NAME, contentValues, KEY_ID + "=" + animal.id, null)

        db.close()
        return success // результат запроса (больше -1 успех)
    }

    // Удаление записи (Delete)
    fun deleteAnimal(animal: AnimalEntity): Int {
        val db = writableDatabase // на чтение

        // удаление строки по id
        val success = db.delete(TABLE_NAME, KEY_ID + "=" + animal.id, null)

        db.close() // закрытие подключения к базе данных
        return success // результат запроса (больше -1 успех)
    }

    fun deleteAllAnimals() {
        val db = writableDatabase // на запись
        db.execSQL("DELETE FROM animal_table")
        db.close()
    }

    // Получение данных (Select)
    fun filterSelect(filterName: String): LiveData<List<AnimalEntity>> {
        val db = readableDatabase // чтение

        val list = mutableListOf<AnimalEntity>() // список для модельного класса
        val listLiveData = MutableLiveData<List<AnimalEntity>>()

        // Запрос на выбор всех записей из таблицы.
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $filterName ASC"

        // Курсор используется для чтения записей по очереди и добавления от каждой коллоны данные в модельный клас
        val cursor: Cursor?

        // составление запроса безопастным способом
        try {
            cursor = db.rawQuery(
                selectQuery,
                null
            ) // выполнение запросу и получение курсора (без выбранных аргументов)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery) // выполняем запроса (без Cursor)
            listLiveData.value = list
            return listLiveData // возрат пустого списка
        }

        // Временные переменные для хранения
        var id: Int
        var name: String
        var age: Int
        var breed: String

        if (cursor.moveToFirst()) { // от первого курсора
            do {
                // получаем данные из курсора
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                age = cursor.getInt(cursor.getColumnIndex(KEY_AGE))
                breed = cursor.getString(cursor.getColumnIndex(KEY_BREED))

                val animal = AnimalEntity(id, name, age, breed) // сохраняем в модель
                list.add(animal)


            } while (cursor.moveToNext()) // прохоид по каждому курсору
        }
        db.close()
        cursor.close()
        listLiveData.value = list


        return listLiveData // возрат списка
    }

}
