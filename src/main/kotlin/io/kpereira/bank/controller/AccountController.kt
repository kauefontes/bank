package io.kpereira.bank.controller

import io.kpereira.bank.model.Event
import io.kpereira.bank.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.lang.Nullable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(private val accountService: AccountService) {

    @RequestMapping(value = ["/balance"], method = [RequestMethod.GET])
    fun balance(account_id: Long): ResponseEntity<Int> {
        if (accountService.exists(account_id))
            return ResponseEntity.ok(accountService.getBalance(account_id))
        return ResponseEntity(0, HttpStatus.NOT_FOUND)
    }

    @RequestMapping(value = ["/reset"], method = [RequestMethod.POST])
    fun reset(): ResponseEntity<String> {
        accountService.clearAllAccounts()
        return ResponseEntity("OK", HttpStatus.OK)
    }

    @RequestMapping(value = ["/event"], method = [RequestMethod.POST])
    fun event(@RequestBody event: Event): ResponseEntity<Any> {
        val origin = event.origin?.toLong()
        if ((event.type == "transfer" && !accountService.exists(origin!!))
            || (event.type == "withdraw" && !accountService.exists(origin!!))
        ) {
            return ResponseEntity(0, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(accountService.delegateEvents(event), HttpStatus.CREATED)
    }
}