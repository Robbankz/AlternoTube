package com.therealbluepandabear.alternotube.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.therealbluepandabear.alternotube.R
import com.therealbluepandabear.alternotube.viewmodels.VideoScreenViewModel

@Composable
fun VideoPlayer(videoSource: String) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
                MediaItem.fromUri(
                    videoSource
                )
            )
            prepare()
            playWhenReady = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Blue),
    ) {
        DisposableEffect(key1 = Unit) {
            onDispose {
                exoPlayer.release()
            }
        }

        AndroidView(
            factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                    layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                }
            }
        )
    }
}

@Composable
fun VideoScreen() {
    val viewModel: VideoScreenViewModel = viewModel()

    Column {
        when {
            viewModel.videoSource != null && viewModel.videoSource!!.data == null -> {
                Text(
                    stringResource(id = R.string.videoScreen_failed_to_play_video,  viewModel.videoSource?.exception.toString())
                )
            }

            viewModel.videoSource != null && viewModel.videoSource!!.data != null -> {
                VideoPlayer(
                    videoSource = viewModel.videoSource!!.data!!
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                viewModel.video?.data?.title ?: "",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                viewModel.video?.data?.channel?.name ?: "",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                "${viewModel.video?.data?.rumbles} rumbles",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}