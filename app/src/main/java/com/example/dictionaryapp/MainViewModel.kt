package com.example.dictionaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.domain.GetWordInfoUsecase
import com.example.dictionaryapp.model.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(private val getWordInfoUsecase: GetWordInfoUsecase) : ViewModel() {

    private var _dictionaryLookupState =
        MutableStateFlow<ApiState<List<WordQuery>>>(ApiState.Default)
    val dictionaryLookupState = _dictionaryLookupState.asStateFlow()

    fun lookupWord(word: String) {
        viewModelScope.launch {
            getWordInfoUsecase.getWordInfo(word).collectLatest {
                _dictionaryLookupState.emit(it)
            }
        }
    }
}