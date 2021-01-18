package io.kpereira.bank.repository

import io.kpereira.bank.model.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account, Long> {
}