package com.therealbluepandabear.alternotube

import com.therealbluepandabear.alternotube.models.RumbleScraper
import org.junit.Test

import org.junit.Assert.*

class VideoSrcTests {
    private val rumbleScraper = RumbleScraper.create()

    @Test
    fun videoSrcIsCorrectForUrl_1() {
        val videoSrc = rumbleScraper.scrapeVideoSource("https://rumble.com/v1ieskt-modded-minecraft-live-stream-ep48-space-training-modpack-lets-play-rumble-e.html")
        assertEquals("https://sp.rmbl.ws/s8/2/T/4/N/C/T4NCf.caa.rec.mp4", videoSrc)
    }

    @Test
    fun videoSrcIsCorrectForUrl_2() {
        val videoSrc = rumbleScraper.scrapeVideoSource("https://rumble.com/v1mh9nz-how-many-takes-jeremy-lynch-football-bloopers-.html")
        assertEquals("https://sp.rmbl.ws/s8/2/V/u/S/2/VuS2f.caa.mp4", videoSrc)
    }

    @Test
    fun videoSrcIsCorrectForUrl_3() {
        val videoSrc = rumbleScraper.scrapeVideoSource("https://rumble.com/v1mll2a-stay-free-with-russell-brand-005-what-is-a-fascist-and-who-decides.html")
        assertEquals("https://sp.rmbl.ws/s8/2/I/F/D/3/IFD3f.caa.rec.mp4", videoSrc)
    }
}