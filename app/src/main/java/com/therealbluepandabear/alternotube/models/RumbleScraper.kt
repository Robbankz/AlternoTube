package com.therealbluepandabear.alternotube.models

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.jsoup.Jsoup
import java.io.IOException

class RumbleScraper private constructor() {
    companion object {
        private const val RUMBLE_URL = "https://rumble.com/"
        private const val RUMBLE_API_URL = "https://rumble.com/embedJS/u3/?request=video&ver=2&v="

        fun create(): RumbleScraper {
            return RumbleScraper()
        }
    }

    fun scrpSearchResults(query: String, page: Int = 1): JsoupResponse<List<RumbleSearchResult>> {
        val exception: Exception?

        try {
            val searchResults = mutableListOf<RumbleSearchResult>()

            val url = if (page <= 1) "${RUMBLE_URL}search/video?q=$query" else "${RUMBLE_URL}search/video?q=$query&page=$page"
            val document = Jsoup.connect(url).get()

            if (document.select("li.video-listing-entry").size == 0) {
                return JsoupResponse(null, emptyList())
            } else {
                for (element in document.select("li.video-listing-entry")) {
                    val searchResult = RumbleSearchResult("", RumbleChannel("", 0, false), 0, "", "")

                    for (element2 in element.select("h3.video-item--title")) {
                        searchResult.title = element2.text()
                    }

                    for (element2 in element.select("address.video-item--by")) {
                        for (element3 in element2.select("div.ellipsis-1")) {
                            searchResult.channel.name = element3.text()

                            if (element3.select("svg.video-item--by-verified verification-badge-icon").isNotEmpty()) {
                                searchResult.channel.isVerified = true
                            }
                        }
                    }

                    for (element2 in element.select("span.video-item--meta video-item--views")) {
                        if (element2.attr("data-value") != "") {
                            searchResult.views = element2.attr("data-value").toString().replace(",", "").toInt()
                        }
                    }

                    for (element2 in element.select("img.video-item--img")) {
                        if (element2.attr("src") != "") {
                            searchResult.thumbnailSrc = element2.attr("src").toString()
                        }
                    }

                    for (element2 in element.select("a.video-item--a")) {
                        if (element2.attr("href") != "") {
                            searchResult.videoUrl = "${RUMBLE_URL}${element2.attr("href")}"
                        }
                    }

                    searchResults.add(searchResult)
                }

                return JsoupResponse(null, searchResults)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, emptyList())
    }

    fun scrpVideoSource(id: String): JsoupResponse<String?> {
        var exception: Exception? = null

        try {
            val document = Jsoup.connect("${RUMBLE_URL}$id").get()

            for (element in document.select("script")) {
                if (element.attr("type") == "application/ld+json") {
                    val content = element.data()
                    val array = JsonParser.parseString(content).asJsonArray

                    val embedUrl = Gson().fromJson(array.get(0).asJsonObject.get("embedUrl"), String::class.java)
                    var embedId = ""

                    for (char in embedUrl.dropLast(1).reversed()) {
                        if (char != '/') {
                            embedId += char
                        } else {
                            break
                        }
                    }

                    val doc = Jsoup.connect("$RUMBLE_API_URL${embedId.reversed()}").ignoreContentType(true).get()
                    val jsonData = doc.select("body").first()?.text()

                    val mp4 = JsonParser.parseString(jsonData).asJsonObject.get("u").asJsonObject.get("mp4").asJsonObject.get("url").toString()

                    return JsoupResponse(null, mp4.replace("\"", ""))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    fun scrpVideoDetails(id: String): JsoupResponse<RumbleVideo?> {
        val exception: Exception?

        try {
            val document = Jsoup.connect("${RUMBLE_URL}$id").get()

            val channel = RumbleChannel(null, 0, false)
            val video = RumbleVideo("", channel, 0, 0, "")

            channel.name = document.select("span.media-heading-name").first()?.text()
            channel.isVerified = document.select("svg.verification-badge-icon media-heading-verified").isNotEmpty()

            video.title = document.title()
            video.rumbles = RumbleScraperUtils.convertShorthandNumberToInt(document.select("div.rumbles-vote").first()?.select("span.rumbles-count")?.first()?.text().toString())

            if (document.select("p.media-description").isNotEmpty()) {
                for (element in document.select("p.media-description")) {
                    video.description += "${element.text()}\n"
                }
            }

            return JsoupResponse(null, video)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }
}
