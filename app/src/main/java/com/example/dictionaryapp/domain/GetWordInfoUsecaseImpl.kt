package com.example.dictionaryapp.domain

import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.data.repository.DictionaryRepository
import com.example.dictionaryapp.model.ApiState
import kotlinx.coroutines.flow.Flow

class GetWordInfoUsecaseImpl(
    private val repository: DictionaryRepository
) : GetWordInfoUsecase {
    override fun getWordInfo(query: String): Flow<ApiState<List<WordQuery>>> {
        return repository.getWordInfo(query)
    }
}