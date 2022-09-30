package com.therealbluepandabear.alternotube.models

data class RumbleSearchResult(
    var title: String?,
    var channel: RumbleChannel,
    var views: Int,
    var thumbnailSrc: String?
)
