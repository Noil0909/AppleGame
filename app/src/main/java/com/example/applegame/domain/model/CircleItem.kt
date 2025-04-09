package com.example.applegame.domain.model

data class CircleItem(
    val index: Int,
    val number: Int,
    val visible: Boolean = true,
    val isSelected: Boolean = false
)