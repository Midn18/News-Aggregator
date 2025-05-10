package com.example.newsAggregator.exceptions

class InvalidSingleFieldException(
    field: String,
    code: String,
) : RuntimeException("$code in field $field")
