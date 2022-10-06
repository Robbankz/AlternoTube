package com.therealbluepandabear.alternotube

import com.therealbluepandabear.alternotube.models.RumbleScraperUtils
import org.junit.Assert
import org.junit.Test

class RumbleScraperUtilsTests {

    @Test
    fun intIsCorrectForShorthandNumber_1() {
        Assert.assertEquals(
            RumbleScraperUtils.convertShorthandNumberToInt("15.5M"),
            15500000
        )
    }
}