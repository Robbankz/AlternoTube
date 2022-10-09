package com.therealbluepandabear.alternotube.models

data class RumbleVideo(
    var title: String? = null,
    var thumbnailSrc: String? = null,
    var channel: RumbleChannel? = null,
    var views: Int? = null,
    var rumbles: Int? = null,
    var uploadDate: String? = null,
    var descriptionHTML: String? = null,
    var videoUrl: String? = null
) {

    val id: String?
        get() {
            return if (videoUrl != null) videoUrl!!.substring(20, videoUrl!!.indexOf("-")) else null
        }
}