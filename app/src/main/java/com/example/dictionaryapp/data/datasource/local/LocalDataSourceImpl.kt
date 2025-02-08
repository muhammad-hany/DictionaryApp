package com.example.dictionaryapp.data.datasource.local

import com.example.dictionaryapp.data.database.DictionaryDao
import com.example.dictionaryapp.data.database.Word
import com.example.dictionaryapp.data.database.WordDefinition
import com.example.dictionaryapp.data.database.WordQuery

class LocalDataSourceImpl(
    private val dao: DictionaryDao
) : LocalDataSource {
    override suspend fun getWordInfo(word: String): List<WordQuery> {
        return dao.getWordInfo(word)
    }

    override suspend fun getWordInfo(id: Long): List<WordQuery> {
        return dao.getWordInfo(id)
    }

    override suspend fun insertWord(word: Word) = dao.insert(word)

    override suspend fun insertDefinitions(wordDefinitions: List<WordDefinition>) {
        dao.insertAll(wordDefinitions)
    }
}