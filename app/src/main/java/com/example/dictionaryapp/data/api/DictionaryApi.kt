package com.example.dictionaryapp.data.api

import com.example.dictionaryapp.model.DictionaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("entries/en/{word}")
    suspend fun getWordInfo(
        @Path("word") word: String
    ): List<DictionaryResponse>

}