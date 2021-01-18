package io.kpereira.bank.service

import io.kpereira.bank.model.Account
import io.kpereira.bank.model.Event
import io.kpereira.bank.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun delegateEvent(event: Event) {
        when (event.type) {
            "deposit" -> deposit(Account(event.destination.toLong(), event.amount))
        }
    }

    fun deposit(account: Account): Account {
        return accountRepository.save(account)
    }

    fun reset() {
        accountRepository.deleteAll()
    }
}