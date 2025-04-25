package com.example.applegame.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.data.record.GameRecord
import com.example.applegame.data.record.GameRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameRecordViewModel(
    private val repository: GameRecordRepository
) : ViewModel() {

    private val _recordList = MutableStateFlow<List<GameRecord>>(emptyList())
    val recordList: StateFlow<List<GameRecord>> = _recordList

    fun loadRecords() {
        viewModelScope.launch {
            val records = repository.getAllRecords()
            _recordList.value = records
        }
    }
}