package com.therealbluepandabear.alternotube.models.rumblescrapers

import com.therealbluepandabear.alternotube.models.*
import org.jsoup.Jsoup

class RumbleSearchScraper private constructor() : SearchScraper {

    override fun scrape(searchQuery: String, page: Int): JsoupResponse<List<RumbleVideo>?> {
        val exception: Exception?

        try {
            val searchResults = mutableListOf<RumbleVideo>()

            val url = if (page <= 1) "${StringConstants.RUMBLE_URL}search/video?q=$searchQuery" else "${StringConstants.RUMBLE_URL}search/video?q=$searchQuery&page=$page"
            val document = Jsoup.connect(url).get()

            if (document.select("li.video-listing-entry").isEmpty()) {
                return JsoupResponse(null, null, StringConstants.JSOUP_RESPONSE_NO_RESULTS_FOUND)
            } else {
                for (element in document.select("li.video-listing-entry")) {
                    val searchResult = RumbleVideo(
                        channel = RumbleChannel(
                            name = element.selectFirst("address.video-item--by")?.selectFirst("div.ellipsis-1")?.text(),
                            isVerified = element.select("svg.video-item--by-verified.verification-badge-icon").isNotEmpty(),
                        ),
                        title = element.selectFirst("h3.video-item--title")?.text(),
                        views = element.selectFirst("span.video-item--meta.video-item--views")?.attr("data-value")?.replace(",", "")?.toInt(),
                        thumbnailSrc = element.selectFirst("img.video-item--img")?.attr("src"),
                        videoUrl = if (element.select("a.video-item--a").isNotEmpty())
                            "${StringConstants.RUMBLE_URL}${element.selectFirst("a.video-item--a")?.attr("href")}"
                        else
                            null
                    )

                    searchResults.add(searchResult)
                }

                return JsoupResponse(null, searchResults)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    companion object {
        fun create(): SearchScraper {
            return RumbleSearchScraper()
        }
    }
}