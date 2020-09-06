package com.exxbrain.arch.web.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class UserDTO(
        @get:NotBlank
        @get:NotNull
        var username: String?,

        @get:NotBlank
        @get:NotNull
        var firstName: String?,

        @get:NotBlank
        @get:NotNull
        var lastName: String?,

        @get:Email
        var email: String?,

        @get:Pattern(regexp = "^\\+(\\d){10,12}$")
        var phone: String?
)