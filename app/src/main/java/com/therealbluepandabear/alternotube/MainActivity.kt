package com.therealbluepandabear.alternotube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import com.therealbluepandabear.alternotube.models.RumbleScraper
import com.therealbluepandabear.alternotube.models.RumbleSearchResult
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
                    Greeting()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {
    var searchResults by rememberSaveable { mutableStateOf(emptyList<RumbleSearchResult>() )}

    Row(
        modifier = Modifier
            .padding(16.dp)
    ) {

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                searchResults = RumbleScraper.create().scrapeSearchResultsFromQuery("Gaming")
            }
        }

        LazyColumn {
            items(searchResults) {
                ListItem(
                    headlineText = {
                        it.title?.let { title ->
                            Text(title)
                        }
                    },

                    supportingText = {
                        it.creator?.let { creator ->
                            Text(creator)
                        }
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
        Greeting()
    }
}