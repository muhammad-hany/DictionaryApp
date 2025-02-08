package com.example.dictionaryapp

import com.example.dictionaryapp.data.database.DictionaryDao
import com.example.dictionaryapp.data.database.Word
import com.example.dictionaryapp.data.database.WordDefinition
import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.data.datasource.local.LocalDataSourceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import org.junit.Test

class LocalDataSourceTest {
    private val dictionaryDao: DictionaryDao = mockk()
    private val localDataSource = LocalDataSourceImpl(dictionaryDao)

    @Test
    fun testGetWordInfo() = runTest {
        val word = "word"
        val result = listOf(WordQuery(word = Word(wordText = word), definitions = emptyList()))
        coEvery { dictionaryDao.getWordInfo(word) } returns result
        assertThat(localDataSource.getWordInfo(word)).isEqualTo(result)
        coVerify { dictionaryDao.getWordInfo(word) }
    }

    @Test
    fun testGetWordInfoForId() = runTest {
        val id = 34L
        val result = listOf(
            WordQuery(
                word = Word(wordText = "word", id = id.toInt()),
                definitions = emptyList()
            )
        )
        coEvery { dictionaryDao.getWordInfo(id) } returns result
        assertThat(localDataSource.getWordInfo(id)).isEqualTo(result)
        coVerify { dictionaryDao.getWordInfo(id) }
    }

    @Test
    fun testInsertWord() = runTest {
        val word = Word(wordText = "word")
        val wordId = 34L
        coEvery { dictionaryDao.insert(word) } returns wordId
        assertThat(localDataSource.insertWord(word)).isEqualTo(wordId)
        coVerify { dictionaryDao.insert(word) }
    }

    @Test
    fun testInsertDefinitions() = runTest {
        val definitions = listOf(
            WordDefinition(
                definition = "definition",
                partOfSpeech = "partOfSpeech",
                wordId = 34
            )
        )
        coEvery { dictionaryDao.insertAll(definitions) } returns Unit
        localDataSource.insertDefinitions(definitions)
        coVerify { dictionaryDao.insertAll(definitions) }

    }

}