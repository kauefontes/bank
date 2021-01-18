package io.kpereira.bank.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
    @Id
    val id: Long = 0L,
    val balance: Int = 0
) {}
