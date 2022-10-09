package com.therealbluepandabear.alternotube.models

data class RumbleChannel(
    var name: String? = null,
    var subscribers: Int? = null,
    var isVerified: Boolean? = null,
    var profileImageSrc: String? = null
)