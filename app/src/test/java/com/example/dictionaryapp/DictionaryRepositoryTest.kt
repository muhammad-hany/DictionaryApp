package com.example.dictionaryapp

import com.example.dictionaryapp.data.database.Word
import com.example.dictionaryapp.data.database.WordDefinition
import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.data.datasource.local.LocalDataSource
import com.example.dictionaryapp.data.datasource.remote.RemoteDataSource
import com.example.dictionaryapp.data.repository.DictionaryRepositoryImpl
import com.example.dictionaryapp.model.ApiState
import com.example.dictionaryapp.model.Definition
import com.example.dictionaryapp.model.DictionaryResponse
import com.example.dictionaryapp.model.Meaning
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class DictionaryRepositoryTest {
    private val remoteDataSource: RemoteDataSource = mockk()
    private val localDataSource: LocalDataSource = mockk()
    private val repository = DictionaryRepositoryImpl(remoteDataSource, localDataSource)

    @Test
    fun `test fetching data from db`() = runTest {
        val word = "word"
        val result = listOf(mockk<WordQuery>())
        coEvery { localDataSource.getWordInfo(word) } returns result
        val collectedResult = mutableListOf<ApiState<List<WordQuery>>>()
        repository.getWordInfo(word).collect {
            collectedResult.add(it)
        }
        assertThat(collectedResult[0]).isEqualTo(ApiState.Loading)
        assertThat(collectedResult[1]).isEqualTo(ApiState.Success(result))
    }

    @Test
    fun `test fetching data from api`() = runTest {
        val word = "word"
        val wordId = 23L
        coEvery { localDataSource.getWordInfo(word) } returns emptyList()
        val definition = "definition"
        val definitions = listOf(Definition(definition = definition))
        val meaning = Meaning(partOfSpeech = "noun", definitions = definitions)
        val dictionaryResponse = DictionaryResponse(
            word = word,
            phonetics = emptyList(),
            meanings = listOf(meaning),
            license = null,
            sourceUrls = emptyList()
        )

        val wordQuery = WordQuery(
            word = Word(wordText = word),
            definitions = listOf(WordDefinition(wordId = wordId, partOfSpeech = "noun", definition = definition))
        )
        val response = listOf(dictionaryResponse)
        coEvery { remoteDataSource.getWordInfo(word) } returns response
        coEvery { localDataSource.insertWord(any()) } returns wordId
        coEvery { localDataSource.insertDefinitions(any()) } returns Unit
        coEvery { localDataSource.getWordInfo(wordId) } returns listOf(wordQuery)


        val collectedResult = mutableListOf<ApiState<List<WordQuery>>>()
        repository.getWordInfo(word).collect {
            collectedResult.add(it)
        }
        // verifying loading state
        assertThat(collectedResult[0]).isEqualTo(ApiState.Loading)
        // verifying success state
        assertThat(collectedResult[1]).isInstanceOf(ApiState.Success::class.java)
        // verifying word text in the success state
        assertThat((collectedResult[1] as ApiState.Success).data.first().word.wordText).isEqualTo(word)
        // verifying definition in the success state
        assertThat((collectedResult[1] as ApiState.Success).data.first().definitions.first().definition).isEqualTo(
            definition
        )

        // verifying that we are getting data from database
        coVerify { localDataSource.getWordInfo(wordId) }

        // verifying that data is being saved in database
        coVerify { localDataSource.insertWord(wordQuery.word) }
        coVerify { localDataSource.insertDefinitions(wordQuery.definitions) }

    }

    @Test
    fun `test network failure`() = runTest {
        val word = "word"
        coEvery { localDataSource.getWordInfo(word) } returns emptyList()
        val errorMessage = "network failure"
        coEvery { remoteDataSource.getWordInfo(word) } throws IOException(errorMessage)
        val collectedResult = mutableListOf<ApiState<List<WordQuery>>>()
        repository.getWordInfo(word).collect {
            collectedResult.add(it)
        }
        assertThat(collectedResult[0]).isEqualTo(ApiState.Loading)
        assertThat(collectedResult[1]).isInstanceOf(ApiState.Error::class.java)
        assertThat((collectedResult[1] as ApiState.Error).throwable.message).isEqualTo(errorMessage)

    }
}