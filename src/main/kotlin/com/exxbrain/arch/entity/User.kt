package com.exxbrain.arch.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "usr")
data class User(
        @Id
        @GeneratedValue(generator = "uuid", strategy= GenerationType.IDENTITY)
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        val id: String?,

        @get:NotBlank
        @get:NotNull
        @Column(unique = true)
        var username: String,

        @get:NotBlank
        @get:NotNull
        var firstName: String,

        @get:NotBlank
        @get:NotNull
        var lastName: String,

        @get:Email
        @Column(unique = true)
        var email: String,

        @Column(unique = true)
        @get:Pattern(regexp = "^\\+(\\d){10,12}$")
        var phone: String
)