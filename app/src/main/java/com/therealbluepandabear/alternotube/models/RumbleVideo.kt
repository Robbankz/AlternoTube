package com.therealbluepandabear.alternotube.models

data class RumbleVideo(
    var title: String? = null,
    var thumbnailSrc: String? = null,
    var channel: RumbleChannel? = null,
    var views: Int? = null,
    var rumbles: Int? = null,
    var uploadDate: String? = null,
    var descriptionHTML: String? = null,
)