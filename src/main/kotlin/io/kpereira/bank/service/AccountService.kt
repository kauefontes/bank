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
            "withdraw" -> withdraw(event.destination.toLong(), event.amount)
            "transfer" -> transfer(event.origin.toLong(), event.amount, event.destination.toLong())
        }
    }

    fun deposit(account: Account): Account {
        val id = account.id
        if (exist(id)) {
            val balance = findById(id).balance
            val amount = account.balance
            val newBalance = balance + amount
            return accountRepository.save(Account(id, newBalance))
        }
        return accountRepository.save(account)
    }

    fun withdraw(id: Long, amount: Int): Account {
        if (exist(id)) {
            val balance = findById(id).balance
            val newBalance = balance + amount
            return accountRepository.save(Account(id, newBalance))
        }
        return findById(id) //404
    }

    fun transfer(origin: Long, amount: Int, destination: Long) {
        if (exist(origin)) {
            val destinationAccount = deposit(Account(destination, amount))
            val originAccount = withdraw(origin, amount)
        }
        //404
    }

    fun findById(id: Long): Account {
        return accountRepository.findById(id).get()
    }

    fun balance(id: Long): Int {
        val account = accountRepository.findById(id).get()
        if (exist(id)) {
            return account.balance
        }
        return account.balance //404
    }

    fun reset() {
        accountRepository.deleteAll()
    }

    fun exist(id: Long): Boolean {
        return accountRepository.existsById(id)
    }
}