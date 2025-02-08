package com.example.dictionaryapp

import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.data.repository.DictionaryRepository
import com.example.dictionaryapp.domain.GetWordInfoUsecaseImpl
import com.example.dictionaryapp.model.ApiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

class GetWordInfoUsecaseTest {
    private val repository: DictionaryRepository = mockk()
    private val getWordInfoUsecaseImpl = GetWordInfoUsecaseImpl(repository)

    @Test
    fun testGetWordInfo() {
        val flow = flowOf(ApiState.Success(data = listOf(mockk<WordQuery>())))
        val word = "word"
        every { repository.getWordInfo(word) } returns flow
        val result = getWordInfoUsecaseImpl.getWordInfo("word")
        assert(result == flow)
    }
}