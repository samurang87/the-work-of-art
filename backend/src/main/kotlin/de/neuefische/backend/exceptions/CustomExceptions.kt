package de.neuefische.backend.exceptions

class NotFoundException(
    message: String,
) : Exception(message)

class UnauthorizedException(
    message: String,
) : Exception(message)
