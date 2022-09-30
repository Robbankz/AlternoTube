package com.therealbluepandabear.alternotube.models

import org.jsoup.Jsoup
import java.io.IOException

class RumbleScraper private constructor() {
    companion object {
        private const val RUMBLE_URL = "https://rumble.com/"

        fun create(): RumbleScraper {
            return RumbleScraper()
        }
    }

    fun scrapeSearchResultsFromQuery(query: String, page: Int = 1): List<RumbleSearchResult> {
        try {
            val searchResults = mutableListOf<RumbleSearchResult>()

            val url = if (page <= 1) "${RUMBLE_URL}search/video?q=$query" else "${RUMBLE_URL}search/video?q=$query&page=$page"
            val document = Jsoup.connect(url).get()

            if (document.getElementsByClass("video-listing-entry").size == 0) {
                return emptyList()
            } else {
                for (element in document.getElementsByClass("video-listing-entry")) {
                    val searchResult = RumbleSearchResult(null, RumbleChannel(null, 0, false), 0, null)

                    for (element2 in element.getElementsByClass("video-item--title")) {
                        searchResult.title = element2.text()
                    }

                    for (element2 in element.getElementsByClass("video-item--by")) {
                        for (element3 in element2.getElementsByClass("ellipsis-1")) {
                            searchResult.channel.name = element3.text()

                            if (element3.getElementsByClass("video-item--by-verified verification-badge-icon").isNotEmpty()) {
                                searchResult.channel.isVerified = true
                            }
                        }
                    }

                    for (element2 in element.getElementsByClass("video-item--meta video-item--views")) {
                        if (element2.attr("data-value") != "") {
                            searchResult.views = element2.attr("data-value").toString().replace(",", "").toInt()
                        }
                    }

                    for (element2 in element.getElementsByClass("video-item--img")) {
                        if (element2.attr("src") != "") {
                            searchResult.thumbnailSrc = element2.attr("src").toString()
                        }
                    }

                    searchResults.add(searchResult)
                }

                return searchResults
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

        return emptyList()
    }
}
