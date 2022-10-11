package com.therealbluepandabear.alternotube.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealbluepandabear.alternotube.models.*
import com.therealbluepandabear.alternotube.models.rumblescraper.RumbleCategoriesScraper
import com.therealbluepandabear.alternotube.models.rumblescraper.RumbleTopVideoScraper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    private val dispatcher = Dispatchers.IO

    private val rumbleCategoriesScraper = RumbleCategoriesScraper.createInstance()
    private val rumbleTopVideoScraper = RumbleTopVideoScraper.createInstance()

    var topVideo: RumbleVideo by mutableStateOf(RumbleVideo())
    var categories: Map<RumbleCategory, List<RumbleVideo>?> by mutableStateOf(emptyMap())

    init {
        scrapeTopVideo()
        scrapeCategories()
    }

    private fun scrapeTopVideo() {
        viewModelScope.launch(dispatcher) {
            rumbleTopVideoScraper.scrape().apply {
                topVideo = this.data ?: RumbleVideo()
            }
        }
    }

    private fun scrapeCategories() {
        viewModelScope.launch(dispatcher) {
            rumbleCategoriesScraper.scrape().apply {
                categories = this.data ?: emptyMap()
            }
        }
    }
}