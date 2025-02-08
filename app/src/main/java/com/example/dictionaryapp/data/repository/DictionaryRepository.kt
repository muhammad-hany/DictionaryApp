package com.example.dictionaryapp.data.repository

import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.model.ApiState
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun getWordInfo(query: String): Flow<ApiState<List<WordQuery>>>
}