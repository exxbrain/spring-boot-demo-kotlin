package com.exxbrain.arch.model.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@Entity
@Table(name = "usr")
data class User(
        @Id
        @GeneratedValue(generator = "uuid", strategy=GenerationType.IDENTITY)
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        val id: String,

        @NotBlank
        @NotNull
        @Column(unique = true)
        val username: String,

        @NotBlank
        @NotNull
        val firstName: String,

        @NotBlank
        @NotNull
        val lastName: String,

        @Email(message = "Email should be valid")
        @NotBlank
        @NotNull
        @Column(unique = true)
        val email: String,

        @Column(unique = true)
        @Pattern(regexp = "^\\d{10,12}$", message = "Phone should be valid")
        val phone: String
)