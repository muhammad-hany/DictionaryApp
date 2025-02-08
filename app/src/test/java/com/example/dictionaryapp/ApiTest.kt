package com.example.dictionaryapp

import com.example.dictionaryapp.data.api.DictionaryApi
import com.example.dictionaryapp.data.network.RetrofitClient
import com.example.dictionaryapp.model.DictionaryResponse
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapter
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class ApiTest {

    private val mockWebServer =  MockWebServer()
    private lateinit var api: DictionaryApi

    private val baseUrl = mockWebServer.url("/").toString()
    private val retrofitClient = RetrofitClient(baseUrl)
    private val moshi = retrofitClient.buildMoshiInstance()
    private val retrofit = retrofitClient.buildRetrofitInstance(moshi)

    @Before
    fun setup() {
        // Create an instance of our ApiService
        api = retrofit.create(DictionaryApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetWordInfo() = runTest {
        val responseObject = DictionaryResponse(
            word = "good",
            phonetics = emptyList(),
            meanings = emptyList(),
            license = null,
            sourceUrls = emptyList()
        )
        val listType = Types.newParameterizedType(List::class.java, DictionaryResponse::class.java)
        val adapter = moshi.adapter<List<DictionaryResponse>>(listType)
        val responseString =  adapter.toJson(listOf(responseObject))

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseString)

        mockWebServer.enqueue(mockResponse)
        val dictionaryResponse = api.getWordInfo("good")
        assertThat(dictionaryResponse).isNotEmpty()
        assertThat(dictionaryResponse.first().word).isEqualTo("good")
    }


}