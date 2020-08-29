package com.exxbrain.arch.controller

import org.springframework.http.ResponseEntity
import org.springframework.transaction.TransactionSystemException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler {
    class FieldValidationError(violation: ConstraintViolation<*>) {
        val propertyPath: String = violation.propertyPath.toString()
        val message: String = violation.message
    }

    @ExceptionHandler(TransactionSystemException::class)
    fun handleConstraintViolation(ex: TransactionSystemException): ResponseEntity<List<FieldValidationError>> {
        val cause = ex.rootCause
        if (cause is ConstraintViolationException) {
            val constraintViolations = cause.constraintViolations
            val errors = constraintViolations.map { FieldValidationError(it) }
            return ResponseEntity.badRequest().body(errors)
        }
        throw ex
    }
}