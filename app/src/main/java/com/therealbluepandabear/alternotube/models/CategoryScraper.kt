package com.therealbluepandabear.alternotube.models

interface CategoryScraper {
    fun scrape(category: RumbleCategory): JsoupResponse<List<RumbleVideo>?>
}