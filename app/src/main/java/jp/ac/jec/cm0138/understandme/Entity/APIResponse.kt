package jp.ac.jec.cm0138.understandme.Entity

import kotlinx.serialization.Serializable

@Serializable
data class APIResponse<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null
)
