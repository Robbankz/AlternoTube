package com.therealbluepandabear.alternotube.models

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.jsoup.Jsoup

class RumbleScraper private constructor() {
    companion object {
        private const val RUMBLE_URL = "https://rumble.com/"
        private const val RUMBLE_API_URL = "https://rumble.com/embedJS/u3/?request=video&ver=2&v="

        fun create(): RumbleScraper {
            return RumbleScraper()
        }
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
                        if (jsonData?.contains("m3u8") == false)
                            JsonParser.parseString(jsonData).asJsonObject.get("u").asJsonObject.get("mp4").asJsonObject.get("url").toString()
                        else
                            JsonParser.parseString(jsonData).asJsonObject.get("u").asJsonObject.get("hls").asJsonObject.get("url").toString()

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

}
