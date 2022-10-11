package com.therealbluepandabear.alternotube.models.rumblescrapers

import com.therealbluepandabear.alternotube.models.JsoupResponse
import com.therealbluepandabear.alternotube.models.RumbleChannel
import com.therealbluepandabear.alternotube.models.RumbleVideo
import com.therealbluepandabear.alternotube.models.StringConstants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Pattern

class RumbleTopVideoScraper {

    private fun scrapeProfileImageSrc(document: Document): String? {
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

        return profileImageSrc
    }

    fun scrape(): JsoupResponse<RumbleVideo?> {
        val exception: Exception?

        try {
            val document = Jsoup.connect(StringConstants.RUMBLE_URL).get()

            val video = RumbleVideo(
                channel = RumbleChannel(
                    name = document.selectFirst("h4.mediaList-by-heading")?.text(),
                    profileImageSrc = scrapeProfileImageSrc(document)
                ),
                title = document.selectFirst("h3.mediaList-heading.size-xlarge")?.text(),
                thumbnailSrc = document.select("img.mediaList-image").attr("src"),
                views = document.selectFirst("small.mediaList-plays")?.text()?.replace(" views", "")?.replace(",", "").toString().toIntOrNull(),
                uploadDate = document.selectFirst("small.mediaList-timestamp")?.text()
            )

            return JsoupResponse(null, video)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }

        return JsoupResponse(exception, null)
    }

    companion object {
        fun createInstance(): RumbleTopVideoScraper {
            return RumbleTopVideoScraper()
        }
    }
}