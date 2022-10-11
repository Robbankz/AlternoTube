package com.therealbluepandabear.alternotube.models.rumblescraper

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.therealbluepandabear.alternotube.models.*
import org.jsoup.Jsoup

class RumbleVideoSourceScraper private constructor() : VideoSourceScraper {

    override fun scrape(channelId: String): JsoupResponse<String?> {
        var exception: Exception? = null

        try {
            val document = Jsoup.connect("${RumbleScraperConstants.RUMBLE_URL}$channelId").get()

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

                    val doc = Jsoup.connect("${RumbleScraperConstants.RUMBLE_API_URL}${embedId.reversed()}")
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

    companion object {
        fun createInstance(): VideoSourceScraper {
            return RumbleVideoSourceScraper()
        }
    }
}