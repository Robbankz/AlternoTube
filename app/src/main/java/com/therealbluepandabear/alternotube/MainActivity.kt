package com.therealbluepandabear.alternotube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.therealbluepandabear.alternotube.screens.HomeScreen
import com.therealbluepandabear.alternotube.screens.VideoScreen
import com.therealbluepandabear.alternotube.ui.theme.AlternoTubeTheme

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
                    AlternoTube()
                }
            }
        }
    }

    @Composable
    private fun AlternoTube() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "home") {
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