package com.therealbluepandabear.alternotube

import com.therealbluepandabear.alternotube.models.VideoDetailsScraper
import com.therealbluepandabear.alternotube.models.rumblescrapers.RumbleVideoDetailsScraper
import org.junit.Test

import org.junit.Assert.*

class RumbleVideoDetailsScraperTests {

    private val rumbleVideoDetailsScraper: VideoDetailsScraper = RumbleVideoDetailsScraper.createInstance()

    @Test
    fun detailsAreCorrectForId_1() {
        val jsoupResponse = rumbleVideoDetailsScraper.scrape("v1k8g1l")
        val videoDetails = jsoupResponse.data
        val channel = videoDetails!!.channel!!

        assertEquals(
            channel.name!!,
            "Reptoid Discovers Minecraft"
        )

        assertEquals(
            channel.isVerified,
            false
        )

        assertTrue(
            videoDetails.rumbles in 1..10
        )
    }
}