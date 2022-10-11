package com.therealbluepandabear.alternotube.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.therealbluepandabear.alternotube.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.therealbluepandabear.alternotube.models.RumbleCategory
import com.therealbluepandabear.alternotube.models.RumbleVideo
import com.therealbluepandabear.alternotube.viewmodels.HomeScreenViewModel
import androidx.compose.ui.layout.ContentScale

@Composable
private fun TopVideo(
    onVideoTapped: (RumbleVideo) -> Unit
) {
    val viewModel: HomeScreenViewModel = viewModel()

    Text(
        stringResource(id = R.string.homeScreen_topVideo).uppercase(),
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
    )

    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                onVideoTapped(viewModel.topVideo)
            }
    ) {
        Column {
            AsyncImage(
                model = viewModel.topVideo.thumbnailSrc,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    viewModel.topVideo.title ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        viewModel.topVideo.channel?.profileImageSrc,
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp, 35.dp)
                            .clip(CircleShape)
                    )

                    Spacer(
                        Modifier.width(8.dp)
                    )

                    Column {
                        Text(
                            viewModel.topVideo.channel?.name ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        if (viewModel.topVideo.views != null) {
                            Text(
                                stringResource(id = R.string.generic_views, viewModel.topVideo.views ?: ""),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        Text(
                            viewModel.topVideo.uploadDate ?: "",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Category(
    category: RumbleCategory,
    onVideoTapped: (RumbleVideo) -> Unit
) {
    val viewModel: HomeScreenViewModel = viewModel()

    LazyRow {
        items(viewModel.categories[category] ?: emptyList()) {
            ElevatedCard(
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .clickable {
                        onVideoTapped(it)
                    }
            ) {
                Column {
                    AsyncImage(
                        it.thumbnailSrc,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            it.title ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Text(
                            it.channel?.name ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryLabel(category: RumbleCategory) {
    val stringResource = when(category) {
        RumbleCategory.EditorPicks -> {
            stringResource(id = R.string.homeScreen_editorPicks)
        }
        
        RumbleCategory.News -> {
            stringResource(id = R.string.homeScreen_news)
        }

        RumbleCategory.Viral -> {
            stringResource(id = R.string.homeScreen_viral)
        }

        RumbleCategory.Finance -> {
            stringResource(id = R.string.homeScreen_finance)
        }

        else -> {
            stringResource(id = R.string.homeScreen_podcasts)
        }
    }

    Text(
        stringResource.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 0.dp),
    )
}

@Composable
fun SearchButton(
    modifier: Modifier,
    onSearchTapped: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onSearchTapped()
        },
        modifier = modifier
    ) {
        Icon(
            Icons.Filled.Search,
            stringResource(id = R.string.homeScreen_search_content_description)
        )
    }
}

@Composable
fun Categories(onVideoTapped: (String) -> Unit) {
    for (category in RumbleCategory.values()) {
        CategoryLabel(category)
        Category(category) {
            it.id?.let { id ->
                onVideoTapped.invoke(id)
            }
        }
    }
}

@Composable
fun HomeScreen(
    onSearchTapped: () -> Unit,
    onVideoTapped: (String) -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {

            TopVideo {
                it.id?.let { id ->
                    onVideoTapped(id)
                }
            }
            Categories(onVideoTapped)
        }

        SearchButton(
            Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            onSearchTapped()
        }
    }
}