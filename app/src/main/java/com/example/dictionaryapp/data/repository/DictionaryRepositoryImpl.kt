package com.example.dictionaryapp.data.repository

import com.example.dictionaryapp.data.database.Word
import com.example.dictionaryapp.data.database.WordDefinition
import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.data.datasource.local.LocalDataSource
import com.example.dictionaryapp.data.datasource.remote.RemoteDataSource
import com.example.dictionaryapp.model.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DictionaryRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : DictionaryRepository {

    override fun getWordInfo(query: String): Flow<ApiState<List<WordQuery>>> = flow {
        emit(ApiState.Loading)
        // checking in the database
        val localData = localDataSource.getWordInfo(query)
        if (localData.isNotEmpty()) {
            emit(ApiState.Success(localData))
        } else {
            try {
                val response = remoteDataSource.getWordInfo(query)
                val wordSearch = response.mapNotNull { dictionaryResponse ->
                    if (dictionaryResponse.word == null || dictionaryResponse.meanings.isEmpty()) return@mapNotNull null
                    dictionaryResponse.meanings.mapNotNull { meaning ->
                        val definition = meaning.definitions.getOrNull(0)?.definition
                        if (meaning.partOfSpeech != null && definition != null) {
                            Word(
                                wordText = dictionaryResponse.word
                            ) to
                            WordDefinition(
                                wordId = -1,
                                partOfSpeech = meaning.partOfSpeech,
                                definition = definition
                            )
                        } else null
                    }
                }.flatten()
                // saving Words entities
                val wordsIdsWithDefinition = wordSearch.map {
                    val wordId = localDataSource.insertWord(it.first)
                    wordId to it.second.copy(wordId = wordId)
                }
                // saving WordDefinition entities with updated wordId
                localDataSource.insertDefinitions(wordsIdsWithDefinition.map { it.second })

                // getting data from database to be single source of truth
                val wordQuery = wordsIdsWithDefinition.map { localDataSource.getWordInfo(it.first) }.flatten()
                emit(ApiState.Success(wordQuery))
            } catch (e: Exception) {
                emit(ApiState.Error(e))
            }
        }
    }

}