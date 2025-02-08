package com.example.dictionaryapp.model

data class DictionaryResponse (
    val word: String? = null,
    val phonetics: List<Phonetic> = emptyList(),
    val meanings: List<Meaning>  = emptyList(),
    val license: License2? = null,
    val sourceUrls: List<String> = emptyList(),
)

data class Phonetic(
    val audio: String? = null,
    val sourceUrl: String? = null,
    val license: License? = null,
    val text: String? = null,
)

data class License(
    val name: String? = null,
    val url: String? = null,
)

data class Meaning(
    val partOfSpeech: String? = null,
    val definitions: List<Definition> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
)

data class Definition(
    val definition: String? = null,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val example: String? = null,
)

data class License2(
    val name: String? = null,
    val url: String? = null,
)
