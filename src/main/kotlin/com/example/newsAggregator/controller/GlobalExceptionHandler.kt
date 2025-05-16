package com.example.newsAggregator.controller

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidation(ex: ConstraintViolationException): ResponseEntity<String> {
        val message = ex.constraintViolations.joinToString("; ") { it.message }
        return ResponseEntity(message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArg(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(ex.message ?: "Invalid request", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleInvalidArgument(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        val message = ex.bindingResult.allErrors.joinToString("; ") { it.defaultMessage ?: "Invalid value" }
        return ResponseEntity(message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<String> {
        return ResponseEntity(
            "Invalid type for parameter '${ex.name}': expected ${ex.requiredType?.simpleName}",
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(ex: MissingServletRequestParameterException): ResponseEntity<String> {
        return ResponseEntity("Missing request parameter: ${ex.parameterName}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<String> {
        return ResponseEntity("Unexpected error: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}