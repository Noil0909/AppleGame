package com.example.applegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        viewModelScope.launch {
            loadInitialData()
            _isLoading.value = false
        }
    }

    private suspend fun loadInitialData() {
        // 실제 로딩 로직
//        loadUserSession()
//        preloadResources()
        delay(500) // 혹시 아주 잠깐의 부드러운 UX 연출을 원하면 추가
    }
}