package com.therealbluepandabear.alternotube.models

interface VideoDetailsScraper {
    fun scrape(videoId: String): JsoupResponse<RumbleVideo?>
}