package com.therealbluepandabear.alternotube.models.rumblescrapers

import com.therealbluepandabear.alternotube.models.*
import org.jsoup.Jsoup

class RumbleCategoriesScraper private constructor() {

    fun scrape(): JsoupResponse<Map<RumbleCategory, List<RumbleVideo>?>?> {
        val exception: Exception?

        try {
            val map = mutableMapOf<RumbleCategory, List<RumbleVideo>?>()
            val categoryScraper = RumbleCategoryScraper.createInstance()

            for (category in RumbleCategory.values()) {
                val jsoupResponse = categoryScraper.scrape(category)

                map[category] = jsoupResponse.data
            }

            return JsoupResponse(Exception(), map)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    private class RumbleCategoryScraper private constructor() : CategoryScraper {

        override fun scrape(category: RumbleCategory): JsoupResponse<List<RumbleVideo>?> {
            val exception: Exception?

            try {
                val document = Jsoup.connect(StringConstants.RUMBLE_URL).get()

                val videos = mutableListOf<RumbleVideo>()

                val elements = document.select("section.one-thirds")[category.index]?.select("li.mediaList-item") ?: return JsoupResponse(null, null)

                for ((index, element) in elements.withIndex()) {
                    val isFirstIndex = index == 0

                    val video = RumbleVideo(
                        channel = RumbleChannel(
                            name = element.selectFirst("h4.mediaList-by-heading")?.text()
                        ),
                        title =
                            if (isFirstIndex) element.selectFirst("h3.mediaList-heading.size-large")?.text()
                            else element.selectFirst("h3.mediaList-heading.size-small")?.text(),
                        thumbnailSrc = element.selectFirst("img.mediaList-image")?.attr("src"),
                        videoUrl = StringConstants.RUMBLE_URL +
                            if (isFirstIndex) element.selectFirst("a.mediaList-link.size-large")?.attr("href")
                            else element.selectFirst("a.mediaList-link.size-small")?.attr("href")
                    )

                    videos.add(video)
                }

                return JsoupResponse(null, videos)
            } catch (e: Exception) {
                e.printStackTrace()
                exception = e
            }

            return JsoupResponse(exception, null)
        }

        companion object {
            fun createInstance(): CategoryScraper {
                return RumbleCategoryScraper()
            }
        }
    }

    companion object {
        fun createInstance(): RumbleCategoriesScraper {
            return RumbleCategoriesScraper()
        }
    }
}