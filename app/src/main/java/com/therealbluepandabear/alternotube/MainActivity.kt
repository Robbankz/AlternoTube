package com.therealbluepandabear.alternotube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.therealbluepandabear.alternotube.screens.HomeScreen
import com.therealbluepandabear.alternotube.screens.VideoScreen
import com.therealbluepandabear.alternotube.ui.theme.AlternoTubeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlternoTubeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        stringResource(id = R.string.app_name),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                },
                            )
                        },
                        content = { innerPadding ->
                            AlternoTube(modifier = Modifier.padding(innerPadding))
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun AlternoTube(modifier: Modifier) {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "home", modifier = modifier) {
            composable(route = "home") {
                HomeScreen { videoId ->
                    navController.navigate("home/${videoId}")
                }
            }

            composable(
                route = "home/{videoId}",
                arguments = listOf(navArgument("videoId") {
                    type = NavType.StringType
                })
            ) {
                VideoScreen()
            }
        }
    }
}