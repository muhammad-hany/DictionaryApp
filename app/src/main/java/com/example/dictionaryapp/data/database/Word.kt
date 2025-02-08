package com.example.dictionaryapp.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wordText: String,
)

@Entity
data class WordDefinition(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wordId: Long,
    val partOfSpeech: String,
    val definition: String,
)

data class WordQuery (
    @Embedded val word: Word,
    @Relation(
        parentColumn = "id",
        entityColumn = "wordId",
    ) val definitions: List<WordDefinition>
)