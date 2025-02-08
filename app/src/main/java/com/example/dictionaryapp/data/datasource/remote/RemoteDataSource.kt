package com.example.dictionaryapp.data.datasource.remote

import com.example.dictionaryapp.model.DictionaryResponse

interface RemoteDataSource {
    suspend fun getWordInfo(word: String): List<DictionaryResponse>
}