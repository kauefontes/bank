package io.kpereira.bank.service

import io.kpereira.bank.model.*
import io.kpereira.bank.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun getBalance(id: Long): Int {
        return accountRepository.findById(id).get().balance
    }

    fun clearAllAccounts() {
        accountRepository.deleteAll()
    }

    fun delegateEvents(event: Event): Any? {
        return when (event.type) {
            "deposit" -> doDeposit(event)
            "withdraw" -> doWithdraw(event)
            else -> doTransfer(event)
        }
    }

    fun doDeposit(event: Event): DepositResponse {
        val account = Account(event.destination!!.toLong(), event.amount!!)
        val id = account.id
        if (exists(id)) {
            val balance = findById(id).balance
            val amount = account.balance
            val newBalance = balance + amount
            return DepositResponse(accountRepository.save(Account(id, newBalance)))
        }
        return DepositResponse(accountRepository.save(account))
    }

    fun doWithdraw(event: Event): WithdrawResponse? {
        val id = event.origin?.toLong()
        val amount = event.amount
        val balance = findById(id!!).balance
        val newBalance = balance - amount!!
        if (newBalance < -500) {
            return null
        }
        return WithdrawResponse(accountRepository.save(Account(id, newBalance)))
    }

    fun doTransfer(event: Event): TransferResponse? {
        val destinationAccount = doDeposit(event)
        val originAccount = doWithdraw(event) ?: return null
        return TransferResponse(originAccount.origin, destinationAccount.destination)
    }

    fun findById(id: Long): Account {
        return accountRepository.findById(id).get()
    }

    fun exists(id: Long): Boolean {
        return accountRepository.existsById(id)
    }
}