package com.nanys.care

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.nanys.care.core.ui.theme.NanysTheme
import com.nanys.care.presentation.navigation.NanysNavGraph
import com.nanys.care.presentation.navigation.NavRoutes
import com.nanys.care.presentation.navigation.startDestinationForRole
import com.nanys.care.presentation.viewmodel.NanysViewModel
import com.nanys.care.presentation.viewmodel.NanysViewModelFactory

class MainActivity : ComponentActivity() {

    private val notificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* demo: no action required */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val container = (application as NanysApplication).container
        val factory = NanysViewModelFactory(container)

        setContent {
            val navController = rememberNavController()
            val viewModel: NanysViewModel = viewModel(factory = factory)
            val currentUser by viewModel.currentUser.collectAsState()

            NanysTheme(role = currentUser?.role ?: viewModel.userRole) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val start = if (viewModel.isLoggedIn) {
                        startDestinationForRole(viewModel.userRole)
                    } else NavRoutes.LOGIN

                    NanysNavGraph(
                        navController = navController,
                        viewModel = viewModel,
                        startDestination = start
                    )
                }
            }
        }
    }
}
