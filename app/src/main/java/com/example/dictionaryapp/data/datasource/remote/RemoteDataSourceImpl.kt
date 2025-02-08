package com.example.dictionaryapp.data.datasource.remote

import com.example.dictionaryapp.data.api.DictionaryApi
import com.example.dictionaryapp.model.DictionaryResponse

class RemoteDataSourceImpl(private val dictionaryApi: DictionaryApi) : RemoteDataSource {

    override suspend fun getWordInfo(word: String): List<DictionaryResponse> {
        return dictionaryApi.getWordInfo(word)
    }
}