package com.therealbluepandabear.alternotube

import com.therealbluepandabear.alternotube.models.RumbleScraperUtils
import org.junit.Assert
import org.junit.Test

class RumbleScraperUtilsTests {

    @Test
    fun intIsCorrectForShorthandNumber_1() {
        Assert.assertEquals(
            RumbleScraperUtils.convertShorthandNumberToInt("15.5M"),
            15_500_000
        )
    }

    @Test
    fun intIsCorrectForShorthandNumber_2() {
        Assert.assertEquals(
            RumbleScraperUtils.convertShorthandNumberToInt("2.59M"),
            2_590_000
        )
    }

    @Test
    fun intIsCorrectForShorthandNumber_3() {
        Assert.assertEquals(
            RumbleScraperUtils.convertShorthandNumberToInt("9M"),
            9_000_000
        )
    }

    @Test
    fun intIsCorrectForShorthandNumber_4() {
        Assert.assertEquals(
            RumbleScraperUtils.convertShorthandNumberToInt("0.5M"),
            500_000
        )
    }

    @Test
    fun intIsCorrectForShorthandNumber_5() {
        Assert.assertEquals(
            RumbleScraperUtils.convertShorthandNumberToInt("100.24M"),
            100_240_000
        )
    }
}