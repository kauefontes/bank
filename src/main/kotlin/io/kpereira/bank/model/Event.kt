package io.kpereira.bank.model

abstract class Event(
    val type: String,
    val origin: String,
    val destination: String,
    val amount: Int,
)