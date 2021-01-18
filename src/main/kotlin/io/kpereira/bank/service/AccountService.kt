package io.kpereira.bank.service

import io.kpereira.bank.model.Account
import io.kpereira.bank.model.Event
import io.kpereira.bank.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun getBalance(id: Long): Int {
        val account = accountRepository.findById(id).get()
        if (exists(id)) {
            return account.balance
        }
        return account.balance //404
    }

    fun clearAllAccounts() {
        accountRepository.deleteAll()
    }

    fun delegateEvents(event: Event) {
        when (event.type) {
            "deposit" -> doDeposit(Account(event.destination.toLong(), event.amount))
            "withdraw" -> doWithdraw(event.origin.toLong(), event.amount)
            "transfer" -> doTransfer(event.origin.toLong(), event.amount, event.destination.toLong())
        }
    }

    fun doDeposit(account: Account): Account {
        val id = account.id
        if (exists(id)) {
            val balance = findById(id).balance
            val amount = account.balance
            val newBalance = balance + amount
            return accountRepository.save(Account(id, newBalance))
        }
        return accountRepository.save(account)
    }

    fun doWithdraw(id: Long, amount: Int): Account {
        if (exists(id)) {
            val balance = findById(id).balance
            val newBalance = balance + amount
            return accountRepository.save(Account(id, newBalance))
        }
        return findById(id) //404
    }

    fun doTransfer(origin: Long, amount: Int, destination: Long) {
        if (exists(origin)) {
            val destinationAccount = doDeposit(Account(destination, amount))
            val originAccount = doWithdraw(origin, amount)
        }
        //404
    }

    fun findById(id: Long): Account {
        return accountRepository.findById(id).get()
    }

    fun exists(id: Long): Boolean {
        return accountRepository.existsById(id)
    }
}