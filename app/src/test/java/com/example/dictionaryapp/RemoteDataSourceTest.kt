package com.example.dictionaryapp

import com.example.dictionaryapp.data.api.DictionaryApi
import com.example.dictionaryapp.data.datasource.remote.RemoteDataSourceImpl
import com.example.dictionaryapp.model.DictionaryResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RemoteDataSourceTest {
    private val dictionaryApi: DictionaryApi = mockk()
    private val remoteDataSource = RemoteDataSourceImpl(dictionaryApi)

    @Test
    fun testGetWordsInfo() = runTest {
        val word = "word"
        val result = listOf(mockk<DictionaryResponse>())
        coEvery { dictionaryApi.getWordInfo(word) } returns result
        assertThat(remoteDataSource.getWordInfo(word)).isEqualTo(result)
        coVerify { dictionaryApi.getWordInfo(word) }
    }
}