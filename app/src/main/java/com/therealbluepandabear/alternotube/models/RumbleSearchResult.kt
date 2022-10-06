package com.therealbluepandabear.alternotube.models

data class RumbleSearchResult(
    var title: String,
    var channel: RumbleChannel,
    var views: Int,
    var thumbnailSrc: String,
    var videoUrl: String
) {
    fun getVideoId(): String {
        val subString = videoUrl.substring(20)

        return subString.substring(0, subString.indexOf("-"))
    }
}
