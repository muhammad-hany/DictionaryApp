package com.example.dictionaryapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Word::class, WordDefinition::class], version = 1)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
}