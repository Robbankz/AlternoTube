package com.therealbluepandabear.alternotube.models

data class RumbleVideo(
    var title: String,
    var channel: RumbleChannel,
    var views: Int,
    var rumbles: Int,
    var descriptionHTML: String,
)