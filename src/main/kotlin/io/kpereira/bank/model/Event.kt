package io.kpereira.bank.model

class Event(
    val type: String,
    val origin: String,
    val destination: String,
    val amount: Int,
)