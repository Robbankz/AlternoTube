package com.therealbluepandabear.alternotube.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealbluepandabear.alternotube.models.RumbleVideo
import com.therealbluepandabear.alternotube.models.rumblescrapers.RumbleSearchScraper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchScreenViewModel : ViewModel() {
    private val rumbleSearchScraper = RumbleSearchScraper.create()

    private var page: Int = 1

    var finalizedSearchQuery: Pair<String, List<RumbleVideo>>? by mutableStateOf(null)

    fun scrapeSearchResults(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            rumbleSearchScraper.scrape(query, page).apply {
                finalizedSearchQuery = Pair(query, this.data ?: emptyList())

                Log.d("BEPPER", rumbleSearchScraper.scrape("Hi").toString())
            }
        }
    }

    fun incrementCurrentPage() {
        finalizedSearchQuery?.let {
            page++
            scrapeSearchResults(it.first)
        }
    }

    fun decrementCurrentPage() {
        if (page > 1) {
            finalizedSearchQuery?.let {
                page--
                scrapeSearchResults(finalizedSearchQuery!!.first)
            }
        }
    }

    fun resetCurrentPage() {
        page = 1
    }
}