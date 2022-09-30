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
                    val searchResult = RumbleSearchResult(null, null, 0)

                    for (element2 in element.getElementsByClass("video-item--title")) {
                        searchResult.title = element2.text()
                    }

                    for (element2 in element.getElementsByClass("video-item--by")) {
                        for (element3 in element2.getElementsByClass("ellipsis-1")) {
                            searchResult.creator = element3.text()
                        }
                    }

                    for (element2 in element.getElementsByClass("video-item--meta video-item--views")) {
                        if (element2.text() != "") {
                            searchResult.views = element2.text().toInt()
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
