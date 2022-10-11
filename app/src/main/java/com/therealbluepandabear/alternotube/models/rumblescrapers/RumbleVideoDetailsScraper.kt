package com.therealbluepandabear.alternotube.models.rumblescrapers

import com.therealbluepandabear.alternotube.models.*
import org.jsoup.Jsoup

class RumbleVideoDetailsScraper private constructor() : VideoDetailsScraper {

    override fun scrape(videoId: String): JsoupResponse<RumbleVideo?> {
        val exception: Exception?

        try {
            val document = Jsoup.connect("${StringConstants.RUMBLE_URL}$videoId").get()

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

    companion object {
        fun createInstance(): VideoDetailsScraper {
            return RumbleVideoDetailsScraper()
        }
    }
}