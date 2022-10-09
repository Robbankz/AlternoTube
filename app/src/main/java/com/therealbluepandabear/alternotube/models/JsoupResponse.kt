package com.therealbluepandabear.alternotube.models

data class JsoupResponse<T>(
    val exception: Exception?,
    val data: T,
    val message: String? =
        if (exception != null && data != null) StringConstants.JSOUP_RESPONSE_SUCCESS
        else if (exception != null && data == null) StringConstants.JSOUP_RESPONSE_FAILURE
        else null
)