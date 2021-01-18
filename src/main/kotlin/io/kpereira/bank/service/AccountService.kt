package io.kpereira.bank.service

import io.kpereira.bank.model.Account
import io.kpereira.bank.model.Event
import io.kpereira.bank.repository.AccountRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun delegateEvent(event: Event) {
        when (event.type) {
            "deposit" -> deposit(Account(event.destination.toLong(), event.amount))
        }
    }

    fun deposit(account: Account): Account {
        val id = account.id
        if (exist(id)) {
            val balance = findById(id).balance
            val value = account.balance
            val newBalance = balance + value
            return accountRepository.save(Account(id, newBalance))
        }
        return accountRepository.save(account)
    }

    fun findById(id: Long): Account {
        return accountRepository.findById(id).get()
    }

    fun balance(id: Long): Int {
        val account = accountRepository.findById(id).get()
        return account.balance
    }

    fun reset() {
        accountRepository.deleteAll()
    }

    fun exist(id: Long): Boolean {
        return accountRepository.existsById(id)
    }
}