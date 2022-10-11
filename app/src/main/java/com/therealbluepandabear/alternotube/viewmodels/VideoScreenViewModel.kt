package com.therealbluepandabear.alternotube.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealbluepandabear.alternotube.models.JsoupResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import com.therealbluepandabear.alternotube.models.RumbleVideo
import com.therealbluepandabear.alternotube.models.StringConstants
import com.therealbluepandabear.alternotube.models.rumblescrapers.RumbleVideoDetailsScraper
import com.therealbluepandabear.alternotube.models.rumblescrapers.RumbleVideoSourceScraper

class VideoScreenViewModel(stateHandle: SavedStateHandle) : ViewModel() {
    private val rumbleVideoSourceScraper = RumbleVideoSourceScraper.createInstance()
    private val rumbleVideoDetailsScraper = RumbleVideoDetailsScraper.createInstance()
    private val dispatcher = Dispatchers.IO

    var videoSource: JsoupResponse<String?>? by mutableStateOf(null)
    var video: JsoupResponse<RumbleVideo?>? by mutableStateOf(null)

    init {
        val id = stateHandle.get<String>(StringConstants.NAV_ARGS_VIDEO_ID) ?: ""

        scrapeVideoSource(id)
        scrapeVideoDetailsForId(id)
    }

    private fun scrapeVideoSource(id: String) {
        viewModelScope.launch(dispatcher) {
            videoSource = rumbleVideoSourceScraper.scrape(id)
        }
    }

    private fun scrapeVideoDetailsForId(id: String) {
        viewModelScope.launch(dispatcher) {
            video = rumbleVideoDetailsScraper.scrape(id)
        }
    }
}