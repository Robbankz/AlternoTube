package com.therealbluepandabear.alternotube.models

data class JsoupResponse<T>(val exception: Exception?, val data: T)