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

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _currentPageGroup = MutableStateFlow(0) // 0: 1~5, 1: 6~10
    val currentPageGroup: StateFlow<Int> = _currentPageGroup

    private val _currentPageRange = MutableStateFlow(1..5)
    val currentPageRange: StateFlow<IntRange> = _currentPageRange

    private val pageSize = 20
    val totalPages: Int
        get() = ((_recordList.value.size - 3).coerceAtLeast(0) + pageSize - 1) / pageSize



    fun loadRecords() {
        viewModelScope.launch {
            val records = repository.getAllRecords()
            _recordList.value = records
            updatePageRange()
        }
    }
    fun setPage(page: Int) {
        _currentPage.value = page
    }

    fun updatePageRange() {
        val start = _currentPageGroup.value * 5 + 1
        val end = (start + 4).coerceAtMost(totalPages)
        _currentPageRange.value = start..end
    }

    fun goToNextPageGroup() {
        _currentPageGroup.value += 1
        updatePageRange()
        _currentPage.value = _currentPageRange.value.first // 그룹 이동 시 첫 페이지로 이동
    }

    fun goToPrevPageGroup() {
        if (_currentPageGroup.value > 0) {
            _currentPageGroup.value -= 1
            updatePageRange()
            _currentPage.value = _currentPageRange.value.first
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