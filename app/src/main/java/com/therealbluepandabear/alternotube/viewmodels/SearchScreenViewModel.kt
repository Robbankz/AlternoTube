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

class SearchScreenViewModel : ViewModel() {
    private val rumbleScraper = RumbleScraper.create()
    private var currentPage: Int = 1

    var jsoupResponseScrapeSearchResults: JsoupResponse<List<RumbleSearchResult>?>? by mutableStateOf(null)

    var finalizedSearchQuery: Pair<String, List<RumbleSearchResult>?>? by mutableStateOf(null)

    fun scrapeSearchResults(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            jsoupResponseScrapeSearchResults = rumbleScraper.scrapeSearchResults(query, currentPage)
            jsoupResponseScrapeSearchResults?.let {
                finalizedSearchQuery = Pair(query, it.data)
            }
        }
    }

    fun incrementCurrentPage() {
        finalizedSearchQuery?.let {
            currentPage++
            scrapeSearchResults(it.first)
        }
    }

    fun decrementCurrentPage() {
        if (currentPage > 1) {
            finalizedSearchQuery?.let {
                currentPage--
                scrapeSearchResults(finalizedSearchQuery!!.first)
            }
        }
    }

    fun resetCurrentPage() {
        currentPage = 1
    }
}