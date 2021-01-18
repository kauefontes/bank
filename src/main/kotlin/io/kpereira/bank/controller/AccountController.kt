package io.kpereira.bank.controller

import io.kpereira.bank.model.Event
import io.kpereira.bank.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(private val accountService: AccountService) {

    @RequestMapping(value = ["/balance"], method = [RequestMethod.GET])
    fun getBalance(account_id: Long): ResponseEntity<Int> {
        if (accountService.exists(account_id))
            return ResponseEntity.ok(accountService.getBalance(account_id))
        return ResponseEntity.notFound().build()
    }

    @RequestMapping(value = ["/reset"], method = [RequestMethod.DELETE])
    fun reset(): ResponseEntity<Unit> {
        accountService.clearAllAccounts()
        return ResponseEntity.ok().build()
    }
}