package com.therealbluepandabear.alternotube.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealbluepandabear.alternotube.models.JsoupResponse
import com.therealbluepandabear.alternotube.models.RumbleScraper
import com.therealbluepandabear.alternotube.models.RumbleSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    private val rumbleScraper = RumbleScraper.create()
    private var currentPage: Int = 1

    var finalizedSearchQuery: Pair<String, JsoupResponse<List<RumbleSearchResult>>>? by mutableStateOf(null)

    fun scrapeSearchResults(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            finalizedSearchQuery = Pair(query, rumbleScraper.scrpSearchResults(query, currentPage))
        }
    }

    fun incrementCurrentPage() {
        if (finalizedSearchQuery?.second?.data?.isNotEmpty() == true) {
            currentPage++
            scrapeSearchResults(finalizedSearchQuery!!.first)
        }
    }

    fun decrementCurrentPage() {
        if (currentPage > 1) {
            currentPage--
            scrapeSearchResults(finalizedSearchQuery!!.first)
        }
    }

    fun resetCurrentPage() {
        currentPage = 1
    }
}