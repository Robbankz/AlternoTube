package com.therealbluepandabear.alternotube.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealbluepandabear.alternotube.models.JsoupResponse
import com.therealbluepandabear.alternotube.models.RumbleCategory
import com.therealbluepandabear.alternotube.models.RumbleScraper
import com.therealbluepandabear.alternotube.models.RumbleVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    private val rumbleScraper = RumbleScraper.create()
    private val dispatcher = Dispatchers.IO

    var jsoupResponseFetchTopVideo: JsoupResponse<RumbleVideo?>? by mutableStateOf(null)
    var topVideo: RumbleVideo? by mutableStateOf(null)

    var jsoupResponseFetchCategories: JsoupResponse<Map<RumbleCategory, List<RumbleVideo>>>? by mutableStateOf(null)
    var categoryVideos: Map<RumbleCategory, List<RumbleVideo>> by mutableStateOf(emptyMap())

    init {
        scrapeTopVideo()
        scrapeCategories()
    }

    private fun scrapeTopVideo() {
        viewModelScope.launch(dispatcher) {
            jsoupResponseFetchTopVideo = rumbleScraper.scrapeTopVideo()
            topVideo = jsoupResponseFetchTopVideo?.data
        }
    }

    private fun scrapeCategories() {
        viewModelScope.launch(dispatcher) {
            jsoupResponseFetchCategories = rumbleScraper.scrapeCategories()
            categoryVideos = jsoupResponseFetchCategories?.data ?: emptyMap()
        }
    }
}