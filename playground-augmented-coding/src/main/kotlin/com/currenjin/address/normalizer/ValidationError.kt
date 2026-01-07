package com.currenjin.address.normalizer

class ValidationError(val input: String) : RuntimeException("Validation error")
