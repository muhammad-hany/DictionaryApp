package com.example.dictionaryapp.domain

import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.model.ApiState
import kotlinx.coroutines.flow.Flow

interface GetWordInfoUsecase {
    fun getWordInfo(query: String): Flow<ApiState<List<WordQuery>>>
}