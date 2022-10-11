package com.therealbluepandabear.alternotube.models

interface SearchScraper {
    fun scrape(searchQuery: String, page: Int = 1): JsoupResponse<List<RumbleVideo>?>
}