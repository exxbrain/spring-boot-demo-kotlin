package com.exxbrain.arch.web.user

import com.exxbrain.arch.AppProperties
import com.exxbrain.arch.web.NotFoundException
import com.exxbrain.arch.entity.User
import com.exxbrain.arch.entity.UserRepository
import io.micrometer.core.annotation.Counted
import io.micrometer.core.annotation.Timed
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URL
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/users")
class UserController(private val repository: UserRepository, private val properties: AppProperties) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getAll(): List<User> {
        return repository.findAll().toList()
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun get(@PathVariable id: String): User {
        return user(id)
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun update(@RequestBody user: UserDTO, @PathVariable id: String): User {
        return repository.save(user(id, user))
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun replace(@Validated @RequestBody user: UserDTO, @PathVariable id: String): User {
        return repository.save(user(id, user))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Validated @RequestBody user: UserDTO, request: HttpServletRequest): ResponseEntity<User> {
        val res = repository.save(user(user))
        val url = URL(request.requestURL.toString())
        val portStr = if (url.port != 80) ":${url.port}" else ""
        val link = "${url.protocol}://${url.host}${portStr}${properties.proxyBasePath}${url.path}/${res.id}"
        return ResponseEntity.created(URI.create(link)).body(res);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) {
        repository.deleteById(id)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete() {
        repository.deleteAll()
    }

    private fun user(user: UserDTO): User {
        return User(
                id = null,
                username = user.username!!,
                firstName = user.firstName!!,
                lastName = user.lastName!!,
                email = user.email!!,
                phone = user.phone!!
        )
    }

    private fun user(id: String, user: UserDTO): User {
        val res = user(id)
        if (user.username != null) {
            res.username = user.username!!
        }
        if (user.firstName != null) {
            res.firstName = user.firstName!!
        }
        if (user.lastName != null) {
            res.lastName = user.lastName!!
        }
        if (user.email != null) {
            res.email = user.email!!
        }
        if (user.phone != null) {
            res.phone = user.phone!!
        }
        return res
    }

    private fun user(id: String): User {
        return repository
                .findById(id)
                .orElseThrow { NotFoundException("User with id $id not found") }
    }
}