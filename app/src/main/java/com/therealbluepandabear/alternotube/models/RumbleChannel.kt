package com.therealbluepandabear.alternotube.models

data class RumbleChannel(
    var name: String?,
    val subscribers: Int,
    var isVerified: Boolean
)