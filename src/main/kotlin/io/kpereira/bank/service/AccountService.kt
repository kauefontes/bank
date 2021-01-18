package io.kpereira.bank.service

import io.kpereira.bank.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) {
}