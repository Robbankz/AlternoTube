package com.therealbluepandabear.alternotube.models.rumblescraper

import com.therealbluepandabear.alternotube.models.*
import org.jsoup.Jsoup

class RumbleVideoDetailsScraper private constructor() : VideoDetailsScraper {

    override fun scrape(videoId: String): JsoupResponse<RumbleVideo?> {
        val exception: Exception?

        try {
            val document = Jsoup.connect("${RumbleScraperConstants.RUMBLE_URL}$videoId").get()

            val video = RumbleVideo(
                channel = RumbleChannel(
                    name = document.selectFirst("span.media-heading-name")?.text(),
                    isVerified = document.select("svg.verification-badge-icon.media-heading-verified").isNotEmpty()
                ),
                title = document.title(),
                rumbles = RumbleScraperUtils.convertShorthandNumberToInt(document.selectFirst("div.rumbles-vote")?.selectFirst("span.rumbles-count")?.text().toString()),
                descriptionHTML = document.selectFirst("div.container.content.media-description")?.children().toString()
            )

            return JsoupResponse(null, video)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    companion object {
        fun createInstance(): VideoDetailsScraper {
            return RumbleVideoDetailsScraper()
        }
    }
}