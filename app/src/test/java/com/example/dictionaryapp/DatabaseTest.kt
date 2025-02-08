package com.example.dictionaryapp

import androidx.room.Room
import com.example.dictionaryapp.data.database.DictionaryDao
import com.example.dictionaryapp.data.database.DictionaryDatabase
import com.example.dictionaryapp.data.database.Word
import com.example.dictionaryapp.data.database.WordDefinition
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(manifest= Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DatabaseTest {
    private lateinit var database: DictionaryDatabase
    private lateinit var dao: DictionaryDao

    @Before
    fun setup() {
        val context = RuntimeEnvironment.getApplication()
        database = Room.inMemoryDatabaseBuilder(
            context,
            DictionaryDatabase::class.java
        ).build()

        dao = database.dictionaryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test inserting and retrieving Word`() = runTest {
        val word = Word(wordText = "word")
        val wordId = dao.insert(word)
        assertThat(wordId).isEqualTo(1)
        val dbWord = dao.getWordInfo(wordId)
        assertThat(dbWord.first().word.wordText).isEqualTo(word.wordText)
    }

    @Test
    fun `test inserting and retrieving Definitions`() = runTest {
        val word = Word(wordText = "word", id = 34)
        val wordId = dao.insert(word)
        val definitions = listOf(
            WordDefinition(
                id = 2,
                definition = "definition",
                partOfSpeech = "partOfSpeech",
                wordId = wordId
            )
        )
        dao.insertAll(definitions)
        val dbDefinitions = dao.getWordInfo(34)
        assertThat(dbDefinitions.first().definitions).isEqualTo(definitions)
    }

    @Test
    fun `test retrieving Word with word string`() = runTest {
        val word = Word(wordText = "word")
        val wordId = dao.insert(word)
        val definitions = listOf(
            WordDefinition(
                id = 2,
                definition = "definition",
                partOfSpeech = "partOfSpeech",
                wordId = wordId
            )
        )
        dao.insertAll(definitions)
        val dbDefinitions = dao.getWordInfo("word")
        assertThat(dbDefinitions.first().word.wordText).isEqualTo(word.wordText)
        assertThat(dbDefinitions.first().definitions).isEqualTo(definitions)
    }
}