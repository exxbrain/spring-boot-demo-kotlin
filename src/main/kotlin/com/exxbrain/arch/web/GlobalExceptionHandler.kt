package com.exxbrain.arch.web

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.transaction.TransactionSystemException
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
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

    @ExceptionHandler(NotFoundException::class, EmptyResultDataAccessException::class)
    fun handleNotFoundException(): ResponseEntity<Any> {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<String> {
        return ResponseEntity
                .badRequest()
                .body(if (ex.rootCause != null) ex.rootCause!!.localizedMessage else ex.localizedMessage)
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleConstraintViolation(ex: MethodArgumentNotValidException): ResponseEntity<List<ObjectError>> {
        return ResponseEntity.badRequest().body(ex.bindingResult.allErrors)
    }
}