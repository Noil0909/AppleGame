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

    private val _recordToDelete = MutableStateFlow<GameRecord?>(null)
    val recordToDelete: StateFlow<GameRecord?> = _recordToDelete


    fun loadRecords() {
        viewModelScope.launch {
            val records = repository.getAllRecords()
            _recordList.value = records
        }
    }

    fun deleteRecord(record: GameRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
            loadRecords() // 삭제 후 다시 불러오기
        }
    }

    // 삭제할 기록을 지정하고 다이얼로그를 띄우기
    fun requestDeleteRecord(record: GameRecord) {
        _recordToDelete.value = record
    }

    // 삭제 확정
    fun confirmDeleteRecord() {
        viewModelScope.launch {
            _recordToDelete.value?.let { record ->
                repository.deleteRecord(record)
                _recordToDelete.value = null
                loadRecords()
            }
        }
    }

    // 삭제 취소
    fun cancelDeleteRecord() {
        _recordToDelete.value = null
    }
}