package com.example.studyplay2.domain.utils

sealed class OrderType {

    object Ascending: OrderType()
    object Descending: OrderType()
}