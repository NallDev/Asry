package com.nalldev.asry.util

sealed class AppException(message: String) : Exception(message)

class NoInternetException(message: String) : AppException(message)
class ApiException(message: String) : AppException(message)
class UnknownException(message: String) : AppException(message)