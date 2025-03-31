package com.example.applegame.domain.model

data class Apple(
    val id: Int,
    val number: Int,
    val position: Int,
    val isSelected: Boolean = false
)