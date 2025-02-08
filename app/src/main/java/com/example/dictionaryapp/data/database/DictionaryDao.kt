package com.example.dictionaryapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DictionaryDao {

    @Insert
    suspend fun insert(word: Word): Long

    @Insert
    suspend fun insertAll(words: List<WordDefinition>)

    @Query("SELECT * FROM word WHERE wordText = :word")
    suspend fun getWordInfo(word: String): List<WordQuery>

    @Query("SELECT * FROM word WHERE id = :id")
    suspend fun getWordInfo(id: Long): List<WordQuery>


}