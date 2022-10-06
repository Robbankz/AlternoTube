package com.therealbluepandabear.alternotube.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealbluepandabear.alternotube.models.JsoupResponse
import com.therealbluepandabear.alternotube.models.RumbleScraper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideoScreenViewModel(stateHandle: SavedStateHandle) : ViewModel() {
    private val rumbleScraper = RumbleScraper.create()

    var videoSource: JsoupResponse? by mutableStateOf(null)

    init {
        scrapeVideoSource(stateHandle.get<String>("videoId") ?: "")
    }

    private fun scrapeVideoSource(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            videoSource = rumbleScraper.scrapeVideoSource(id)
        }
    }
}