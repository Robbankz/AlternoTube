package com.therealbluepandabear.alternotube.models

interface VideoSourceScraper {
    fun scrape(channelId: String): JsoupResponse<String?>
}