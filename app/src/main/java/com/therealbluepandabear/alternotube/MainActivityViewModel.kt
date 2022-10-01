package com.therealbluepandabear.alternotube

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.therealbluepandabear.alternotube.models.RumbleSearchResult

class MainActivityViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
    var searchResults by mutableStateOf(emptyList<RumbleSearchResult>())
}