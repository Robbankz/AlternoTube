package com.therealbluepandabear.alternotube.models

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

class RumbleScraper private constructor() {
    companion object {
        private const val RUMBLE_URL = "https://rumble.com/"
        private const val RUMBLE_API_URL = "https://rumble.com/embedJS/u3/?request=video&ver=2&v="

        fun create(): RumbleScraper {
            return RumbleScraper()
        }
    }

    fun scrapeSearchResults(query: String, page: Int = 1): JsoupResponse<List<RumbleSearchResult>?> {
        val exception: Exception?

        try {
            val searchResults = mutableListOf<RumbleSearchResult>()

            val url = if (page <= 1) "${RUMBLE_URL}search/video?q=$query" else "${RUMBLE_URL}search/video?q=$query&page=$page"
            val document = Jsoup.connect(url).get()

            if (document.select("li.video-listing-entry").size == 0) {
                return JsoupResponse(null, null, StringConstants.JSOUP_RESPONSE_NO_RESULTS_FOUND)
            } else {
                for (element in document.select("li.video-listing-entry")) {
                    val searchResult = RumbleSearchResult("", RumbleChannel("", 0, false), 0, "", "")

                    for (element2 in element.select("h3.video-item--title")) {
                        searchResult.title = element2.text()
                    }

                    for (element2 in element.select("address.video-item--by")) {
                        for (element3 in element2.select("div.ellipsis-1")) {
                            searchResult.channel.name = element3.text()

                            if (element3.select("svg.video-item--by-verified verification-badge-icon")
                                    .isNotEmpty()
                            ) {
                                searchResult.channel.isVerified = true
                            }
                        }
                    }

                    for (element2 in element.select("span.video-item--meta.video-item--views")) {
                        searchResult.views =
                            element2.attr("data-value").toString().replace(",", "").toInt()
                    }

                    for (element2 in element.select("img.video-item--img")) {
                        searchResult.thumbnailSrc = element2.attr("src").toString()
                    }

                    for (element2 in element.select("a.video-item--a")) {
                        searchResult.videoUrl = "${RUMBLE_URL}${element2.attr("href")}"
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

    fun scrapeVideoSource(id: String): JsoupResponse<String?> {
        var exception: Exception? = null

        try {
            val document = Jsoup.connect("${RUMBLE_URL}$id").get()

            for (element in document.select("script")) {
                if (element.attr("type") == "application/ld+json") {
                    val content = element.data()
                    val array = JsonParser.parseString(content).asJsonArray

                    val embedUrl = Gson().fromJson(
                        array.get(0).asJsonObject.get("embedUrl"),
                        String::class.java
                    )
                    var embedId = ""

                    for (char in embedUrl.dropLast(1).reversed()) {
                        if (char != '/') {
                            embedId += char
                        } else {
                            break
                        }
                    }

                    val doc = Jsoup.connect("$RUMBLE_API_URL${embedId.reversed()}")
                        .ignoreContentType(true).get()
                    val jsonData = doc.selectFirst("body")?.text()

                    val mp4 =
                        JsonParser.parseString(jsonData).asJsonObject.get("u").asJsonObject.get("mp4").asJsonObject.get(
                            "url"
                        ).toString()

                    return JsoupResponse(null, mp4.replace("\"", ""))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    fun scrapeVideoDetails(id: String): JsoupResponse<RumbleVideo?> {
        val exception: Exception?

        try {
            val document = Jsoup.connect("${RUMBLE_URL}$id").get()

            val channel = RumbleChannel()
            val video = RumbleVideo()

            channel.name = document.selectFirst("span.media-heading-name")?.text()
            channel.isVerified = document.select("svg.verification-badge-icon.media-heading-verified").isNotEmpty()

            video.title = document.title()
            video.rumbles = RumbleScraperUtils.convertShorthandNumberToInt(
                document.selectFirst("div.rumbles-vote")?.selectFirst("span.rumbles-count")?.text().toString()
            )

            video.channel = channel

            for (element in document.select("div.media-description")) {
                video.descriptionHTML += element
            }

            return JsoupResponse(null, video)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    fun scrapeTopVideo(): JsoupResponse<RumbleVideo?> {
        val exception: Exception?

        try {
            val document = Jsoup.connect(RUMBLE_URL).get()

            val video = RumbleVideo()
            val channel = RumbleChannel()

            video.title = document.selectFirst("h3.mediaList-heading.size-xlarge")?.text()
            video.thumbnailSrc = document.select("img.mediaList-image").attr("src")
            video.views =
                document.selectFirst("small.mediaList-plays")?.text()?.replace(" views", "")
                    ?.replace(",", "").toString().toIntOrNull()
            video.uploadDate = document.selectFirst("small.mediaList-timestamp")?.text()

            channel.name = document.selectFirst("h4.mediaList-by-heading")?.text()

            val css = document.selectFirst("style")?.data().toString()
            val toFind = "background-image:"
            val word = Pattern.compile(toFind)
            val matcher = word.matcher(css)

            var i = 0

            var profileImageSrc: String? = null

            while (matcher.find()) {
                i++

                if (i == 2) {
                    val chunk1 = css.substring(matcher.start(), matcher.start() + 100)
                    val chunk2 = chunk1.substring(chunk1.indexOf('(') + 1, chunk1.indexOf(')'))
                    profileImageSrc = chunk2
                    break
                }
            }

            channel.profileImageSrc = profileImageSrc

            video.channel = channel

            return JsoupResponse(null, video)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    private fun scrapeCategory(rumbleCategory: RumbleCategory): JsoupResponse<List<RumbleVideo>> {
        val exception: Exception?

        try {
            val document = Jsoup.connect(RUMBLE_URL).get()

            val videos = mutableListOf<RumbleVideo>()

            val toLoop = document.select("section.one-thirds")[rumbleCategory.index]?.select("li.mediaList-item") ?: emptyList()

            for ((index, element) in toLoop.withIndex()) {
                val video = RumbleVideo()
                video.channel = RumbleChannel()

                video.title = if (index == 0) {
                    element.selectFirst("h3.mediaList-heading.size-large")?.text()
                } else {
                    element.selectFirst("h3.mediaList-heading.size-small")?.text()
                }
                video.thumbnailSrc = element.select("img.mediaList-image").attr("src")
                video.channel?.name = element.selectFirst("h4.mediaList-by-heading")?.text()
                video.videoUrl = RUMBLE_URL + if (index == 0) {
                    element.selectFirst("a.mediaList-link.size-large")?.attr("href")
                } else {
                    element.selectFirst("a.mediaList-link.size-small")?.attr("href")
                }

                videos.add(video)
            }

            return JsoupResponse(null, videos)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, emptyList())
    }

    fun scrapeCategories(): JsoupResponse<Map<RumbleCategory, List<RumbleVideo>>> {
        val exception: Exception?

        try {
            val map = mutableMapOf<RumbleCategory, List<RumbleVideo>>()

            for (category in RumbleCategory.values()) {
                val jsoupResponse = scrapeCategory(category)
                map[category] = jsoupResponse.data
            }

            return JsoupResponse(null, map)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, emptyMap())
    }
}
