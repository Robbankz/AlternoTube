package com.therealbluepandabear.alternotube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.therealbluepandabear.alternotube.models.RumbleScraper
import com.therealbluepandabear.alternotube.ui.theme.AlternoTubeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlternoTubeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainComposable()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainComposable() {
    val viewModel: MainActivityViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.wrapContentSize()
        ) {
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.searchQuery = it },
                label = {
                    Text(
                        stringResource(id = R.string.mainActivity_search)
                    )
                }
            )

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.searchResults = RumbleScraper.create().scrapeSearchResultsFromQuery(viewModel.searchQuery)
                    }
                    keyboardController?.hide()
                },
            ) {
                Text(
                    stringResource(id = R.string.mainActivity_search)
                )
            }
        }

        LazyColumn {
            items(viewModel.searchResults) {
                ListItem(
                    headlineText = {
                        it.title?.let { title ->
                            Text(title)
                        }
                    },

                    supportingText = {
                        it.channel.let { creator ->
                            val text = when {
                                it.views > 0 -> {
                                    "${creator.name}, ${it.views} views"
                                }

                                else -> {
                                    creator.name ?: ""
                                }
                            }

                            Row {
                                Text(text)

                                if (creator.isVerified) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_baseline_verified_24),
                                        tint = Color.Cyan,
                                        contentDescription = stringResource(id = R.string.mainActivity_verified_content_description)
                                    )
                                }
                            }
                        }
                    },

                    leadingContent = {
                        AsyncImage(
                            it.thumbnailSrc,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp, 100.dp)
                        )
                    }
                )
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AlternoTubeTheme {
        MainComposable()
    }
}