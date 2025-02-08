package com.example.dictionaryapp.data.datasource.local

import com.example.dictionaryapp.data.database.WordDefinition
import com.example.dictionaryapp.data.database.Word
import com.example.dictionaryapp.data.database.WordQuery

interface LocalDataSource {
    suspend fun getWordInfo(word: String): List<WordQuery>
    suspend fun getWordInfo(id: Long): List<WordQuery>
    suspend fun insertWord(word: Word): Long
    suspend fun insertDefinitions(wordDefinitions: List<WordDefinition>)
}