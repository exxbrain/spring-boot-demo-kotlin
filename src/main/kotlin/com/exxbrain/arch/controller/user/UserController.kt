package com.exxbrain.arch.controller.user

import com.exxbrain.arch.entity.UserRepository
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RepositoryRestController
class UserController(private val repository: UserRepository) {
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAll() {
        repository.deleteAll()
    }
}