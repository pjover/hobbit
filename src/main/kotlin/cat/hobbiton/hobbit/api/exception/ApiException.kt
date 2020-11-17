package cat.hobbiton.hobbit.api.exception

open class ApiException(msg: String, val code: Int) : Exception(msg)