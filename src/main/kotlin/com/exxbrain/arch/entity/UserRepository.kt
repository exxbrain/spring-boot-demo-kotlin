package com.exxbrain.arch.entity

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource


@RepositoryRestResource
interface UserRepository : CrudRepository<User, String> {
//    fun save(entity: User): User
//    fun findAll(): Iterable<User>
//    fun delete(entity: User)
//    fun findById(id: String): Optional<User>
//    fun deleteAll()
}