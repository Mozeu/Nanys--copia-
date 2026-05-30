package com.nanys.care.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nanys.care.domain.model.UserRole
import com.nanys.care.presentation.admin.AdminDashboardScreen
import com.nanys.care.presentation.admin.CatalogManagementScreen
import com.nanys.care.presentation.auth.LoginScreen
import com.nanys.care.presentation.auth.RegisterScreen
import com.nanys.care.presentation.caregiver.*
import com.nanys.care.presentation.chat.ChatDetailScreen
import com.nanys.care.presentation.chat.ChatListScreen
import com.nanys.care.presentation.settings.SettingsScreen
import com.nanys.care.presentation.supervisor.SupervisorDashboardScreen
import com.nanys.care.presentation.supervisor.VerificationScreen
import com.nanys.care.presentation.tutor.*
import com.nanys.care.presentation.viewmodel.NanysViewModel

@Composable
fun NanysNavGraph(
    navController: NavHostController,
    viewModel: NanysViewModel,
    startDestination: String
) {
    val navigateToRoleHome: (UserRole) -> Unit = { role ->
        val dest = when (role) {
            UserRole.CUIDADOR -> NavRoutes.CAREGIVER_DASH
            UserRole.TUTOR -> NavRoutes.TUTOR_DASH
            UserRole.ADMIN -> NavRoutes.ADMIN_DASH
            UserRole.SUPERVISOR -> NavRoutes.SUPERVISOR_DASH
        }
        navController.navigate(dest) {
            popUpTo(0) { inclusive = true }
        }
    }

    val logout: () -> Unit = {
        viewModel.logout {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onNavigateRegister = { navController.navigate(NavRoutes.REGISTER) },
                onLoginSuccess = navigateToRoleHome
            )
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                viewModel = viewModel,
                onNavigateLogin = { navController.popBackStack() },
                onRegisterSuccess = { role ->
                    when (role) {
                        UserRole.CUIDADOR -> navController.navigate(NavRoutes.CAREGIVER_PROFILE) { popUpTo(0) { inclusive = true } }
                        UserRole.TUTOR -> navController.navigate(NavRoutes.TUTOR_PROFILE) { popUpTo(0) { inclusive = true } }
                        else -> navigateToRoleHome(role)
                    }
                }
            )
        }

        // Cuidador
        composable(NavRoutes.CAREGIVER_DASH) {
            CaregiverDashboardScreen(viewModel, onNavigate = { navController.navigate(it) }, onLogout = logout)
        }
        composable(NavRoutes.CAREGIVER_REQUESTS) {
            CaregiverRequestsScreen(viewModel, onBack = { navController.popBackStack() }, onViewTutor = { navController.navigate(NavRoutes.tutorPrivate(it)) })
        }
        composable(NavRoutes.CAREGIVER_AGENDA) {
            CaregiverAgendaScreen(viewModel, onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.CAREGIVER_PROFILE) {
            CaregiverProfileScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navigateToRoleHome(UserRole.CUIDADOR) }
            )
        }
        composable(NavRoutes.CAREGIVER_REGULATIONS) {
            RegulationsScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.CAREGIVER_PRIVATE_NOTES) {
            PrivateNotesScreen(viewModel, onBack = { navController.popBackStack() })
        }

        // Tutor
        composable(NavRoutes.TUTOR_DASH) {
            TutorDashboardScreen(viewModel, onNavigate = { navController.navigate(it) }, onLogout = logout)
        }
        composable(NavRoutes.TUTOR_SEARCH) {
            SearchCaregiversScreen(viewModel, onBack = { navController.popBackStack() }, onOpenProfile = { navController.navigate(NavRoutes.caregiverPublic(it)) })
        }
        composable(NavRoutes.TUTOR_BOOKINGS) {
            TutorBookingsScreen(viewModel, onBack = { navController.popBackStack() }, onReview = { navController.navigate(NavRoutes.submitReview(it)) })
        }
        composable(NavRoutes.TUTOR_AGENDA) {
            TutorAgendaScreen(viewModel, onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.TUTOR_PROFILE) {
            TutorProfileScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navigateToRoleHome(UserRole.TUTOR) }
            )
        }
        composable(
            route = NavRoutes.BOOK_APPOINTMENT,
            arguments = listOf(navArgument("caregiverEmail") { type = NavType.StringType })
        ) { backStack ->
            val email = backStack.arguments?.getString("caregiverEmail") ?: return@composable
            BookAppointmentScreen(viewModel, email, onBack = { navController.popBackStack() }, onDone = { navController.popBackStack() })
        }
        composable(
            route = NavRoutes.SUBMIT_REVIEW,
            arguments = listOf(navArgument("bookingId") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("bookingId") ?: return@composable
            SubmitReviewScreen(viewModel, id, onBack = { navController.popBackStack() })
        }
        composable(
            route = NavRoutes.CAREGIVER_PUBLIC,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStack ->
            val email = backStack.arguments?.getString("email") ?: return@composable
            CaregiverPublicProfileScreen(
                viewModel, email,
                onBack = { navController.popBackStack() },
                onBook = { navController.navigate(NavRoutes.bookAppointment(email)) },
                onChat = { navController.navigate(NavRoutes.chatDetail(email)) }
            )
        }
        composable(
            route = NavRoutes.TUTOR_PRIVATE,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStack ->
            val email = backStack.arguments?.getString("email") ?: return@composable
            TutorPrivateProfileScreen(viewModel, email, onBack = { navController.popBackStack() })
        }

        // Admin
        composable(NavRoutes.ADMIN_DASH) {
            AdminDashboardScreen(viewModel, onNavigate = { navController.navigate(it) }, onLogout = logout)
        }
        composable(
            route = NavRoutes.ADMIN_CATALOG,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStack ->
            val category = backStack.arguments?.getString("category") ?: return@composable
            CatalogManagementScreen(viewModel, category, onBack = { navController.popBackStack() })
        }

        // Supervisor
        composable(NavRoutes.SUPERVISOR_DASH) {
            SupervisorDashboardScreen(viewModel, onNavigate = { navController.navigate(it) }, onLogout = logout)
        }
        composable(NavRoutes.SUPERVISOR_CHATS) {
            ChatListScreen(viewModel, readOnly = true, onBack = { navController.popBackStack() }, onOpenChat = { navController.navigate(NavRoutes.chatDetail(it)) })
        }
        composable(NavRoutes.SUPERVISOR_VERIFICATION) {
            VerificationScreen(viewModel, onBack = { navController.popBackStack() })
        }

        // Shared
        composable(NavRoutes.CHAT_LIST) {
            ChatListScreen(viewModel, onBack = { navController.popBackStack() }, onOpenChat = { navController.navigate(NavRoutes.chatDetail(it)) })
        }
        composable(
            route = NavRoutes.CHAT_DETAIL,
            arguments = listOf(navArgument("otherEmail") { type = NavType.StringType })
        ) { backStack ->
            val other = backStack.arguments?.getString("otherEmail") ?: return@composable
            val readOnly = viewModel.userRole == UserRole.SUPERVISOR
            ChatDetailScreen(
                viewModel, other, readOnly = readOnly,
                onBack = { navController.popBackStack() },
                onOpenProfile = {
                    when (viewModel.userRole) {
                        UserRole.CUIDADOR -> navController.navigate(NavRoutes.tutorPrivate(it))
                        UserRole.TUTOR -> navController.navigate(NavRoutes.caregiverPublic(it))
                        else -> Unit
                    }
                }
            )
        }
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

fun startDestinationForRole(role: UserRole?): String = when (role) {
    UserRole.CUIDADOR -> NavRoutes.CAREGIVER_DASH
    UserRole.TUTOR -> NavRoutes.TUTOR_DASH
    UserRole.ADMIN -> NavRoutes.ADMIN_DASH
    UserRole.SUPERVISOR -> NavRoutes.SUPERVISOR_DASH
    null -> NavRoutes.LOGIN
}
