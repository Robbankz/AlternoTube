package com.therealbluepandabear.alternotube.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealbluepandabear.alternotube.models.RumbleScraper
import com.therealbluepandabear.alternotube.models.RumbleSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    private val rumbleScraper = RumbleScraper.create()
    private val dispatcher = Dispatchers.IO

    data class SearchQuery(val query: String, var results: List<RumbleSearchResult>)

    var finalizedSearchQuery: SearchQuery? by mutableStateOf(null)

    fun scrapeSearchResults(query: String) {
        viewModelScope.launch(dispatcher) {
            finalizedSearchQuery = SearchQuery(query.trim(), rumbleScraper.scrapeSearchResults(query))
        }
    }
}