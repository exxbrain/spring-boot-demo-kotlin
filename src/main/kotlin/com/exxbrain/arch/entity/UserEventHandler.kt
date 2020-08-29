package com.exxbrain.arch.entity

import org.slf4j.LoggerFactory
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@RepositoryEventHandler
@Component
class UserEventHandler {
    private val log = LoggerFactory.getLogger(UserEventHandler::class.java)

    @HandleBeforeSave
    fun handleUserSave(user: User) {
        log.debug(user.toString())
    }
}