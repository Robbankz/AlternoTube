package com.therealbluepandabear.alternotube.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.therealbluepandabear.alternotube.viewmodels.SearchScreenViewModel
import com.therealbluepandabear.alternotube.R
import com.therealbluepandabear.alternotube.models.RumbleSearchResult
import com.therealbluepandabear.alternotube.models.StringConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RumbleSearchResult(rumbleSearchResult: RumbleSearchResult, onVideoTapped: () -> Unit) {
    ListItem(
        headlineText = {
            Text(rumbleSearchResult.title)
        },

        supportingText = {
            rumbleSearchResult.channel.let { creator ->
                val text = when {
                    rumbleSearchResult.views > 0 -> {
                        "${creator.name}, ${stringResource(id = R.string.generic_views, rumbleSearchResult.views)}"
                    }

                    else -> {
                        creator.name ?: ""
                    }
                }

                Row {
                    Text(text)

                    if (creator.isVerified == true) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_verified_24),
                            tint = Color.Cyan,
                            contentDescription = stringResource(id = R.string.searchScreen_verified_content_description)
                        )
                    }
                }
            }
        },

        leadingContent = {
            AsyncImage(
                rumbleSearchResult.thumbnailSrc,
                contentDescription = null,
                modifier = Modifier.size(100.dp, 100.dp)
            )
        },

        modifier = Modifier.clickable {
            onVideoTapped.invoke()
        }
    )
    Divider()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(onVideoTapped: (String) -> Unit) {
    val viewModel: SearchScreenViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current

    var query: String by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.wrapContentSize()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = {
                    Text(
                        stringResource(id = R.string.searchScreen_search)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
            )

            Button(
                onClick = {
                    if (query.isNotBlank()) {
                        viewModel.resetCurrentPage()
                        viewModel.scrapeSearchResults(query)
                        keyboardController?.hide()
                    }
                },
            ) {
                Text(
                    stringResource(id = R.string.searchScreen_search)
                )
            }
        }

        viewModel.jsoupResponseScrapeSearchResults?.let {
            if (it.message == StringConstants.JSOUP_RESPONSE_NO_RESULTS_FOUND) {
                Text(
                    stringResource(id = R.string.searchScreen_no_results_found)
                )
            } else if (it.exception != null) {
                Text(
                    stringResource(id = R.string.searchScreen_failed_to_load_search_results, it.exception)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(viewModel.finalizedSearchQuery?.second ?: emptyList()) { rumbleSearchResult ->
                        RumbleSearchResult(rumbleSearchResult = rumbleSearchResult) {
                            onVideoTapped(rumbleSearchResult.id)
                        }
                    }
                }
            }
        }

        Row {
            Button(
                onClick = {
                    viewModel.decrementCurrentPage()
                },
            ) {
                Text(
                    stringResource(id = R.string.searchScreen_previous_page)
                )
            }

            Button(
                onClick = {
                    viewModel.incrementCurrentPage()
                },
            ) {
                Text(
                    stringResource(id = R.string.searchScreen_next_page)
                )
            }
        }
    }
}