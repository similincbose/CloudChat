package dev.similin.cloudchat.network

sealed class Status {
    object Success : Status()
    object Error : Status()
    object Loading : Status()
}

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(
                status = Status.Success,
                data = data,
                message = null
            )

        fun <T> error(message: String): Resource<T> =
            Resource(
                status = Status.Error,
                data = null,
                message = message
            )

        fun <T> loading(): Resource<T> =
            Resource(
                status = Status.Loading,
                data = null,
                message = null
            )
    }
}