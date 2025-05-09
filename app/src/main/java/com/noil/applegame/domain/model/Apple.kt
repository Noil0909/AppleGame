package com.noil.applegame.domain.model

data class Apple(
    val id: Int,
    val number: Int,
    val visible: Boolean = true,
    val isSelected: Boolean = false
)