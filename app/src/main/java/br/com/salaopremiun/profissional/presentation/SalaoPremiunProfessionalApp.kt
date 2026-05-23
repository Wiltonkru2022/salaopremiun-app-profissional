package br.com.salaopremiun.profissional.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.salaopremiun.profissional.core.network.ApiClient
import br.com.salaopremiun.profissional.core.session.SessionStore
import br.com.salaopremiun.profissional.data.local.ProfessionalDatabase
import br.com.salaopremiun.profissional.data.repository.ProfessionalAppRepository
import br.com.salaopremiun.profissional.presentation.components.AppChromeHeader
import br.com.salaopremiun.profissional.presentation.components.BottomBar
import br.com.salaopremiun.profissional.presentation.components.ProfessionalAppBackground
import br.com.salaopremiun.profissional.presentation.navigation.AppDestination
import br.com.salaopremiun.profissional.presentation.navigation.bottomDestinations
import br.com.salaopremiun.profissional.presentation.screens.AddCommandItemScreen
import br.com.salaopremiun.profissional.presentation.screens.AgendaScreen
import br.com.salaopremiun.profissional.presentation.screens.AppointmentDetailScreen
import br.com.salaopremiun.profissional.presentation.screens.ChangePasswordScreen
import br.com.salaopremiun.profissional.presentation.screens.ClientDetailScreen
import br.com.salaopremiun.profissional.presentation.screens.ClientFormScreen
import br.com.salaopremiun.profissional.presentation.screens.ClientsScreen
import br.com.salaopremiun.profissional.presentation.screens.CommandDetailScreen
import br.com.salaopremiun.profissional.presentation.screens.CommandsScreen
import br.com.salaopremiun.profissional.presentation.screens.CommissionsScreen
import br.com.salaopremiun.profissional.presentation.screens.DashboardScreen
import br.com.salaopremiun.profissional.presentation.screens.EditAppointmentScreen
import br.com.salaopremiun.profissional.presentation.screens.InstallScreen
import br.com.salaopremiun.profissional.presentation.screens.LegalScreen
import br.com.salaopremiun.profissional.presentation.screens.LoginScreen
import br.com.salaopremiun.profissional.presentation.screens.NewCommandScreen
import br.com.salaopremiun.profissional.presentation.screens.NewAppointmentScreen
import br.com.salaopremiun.profissional.presentation.screens.NotificationsScreen
import br.com.salaopremiun.profissional.presentation.screens.OnboardingScreen
import br.com.salaopremiun.profissional.presentation.screens.PasswordRecoveryScreen
import br.com.salaopremiun.profissional.presentation.screens.PrivacyScreen
import br.com.salaopremiun.profissional.presentation.screens.ProfileDetailsScreen
import br.com.salaopremiun.profissional.presentation.screens.ProfileScreen
import br.com.salaopremiun.profissional.presentation.screens.QuestionsScreen
import br.com.salaopremiun.profissional.presentation.screens.ReviewsScreen
import br.com.salaopremiun.profissional.presentation.screens.SettingsScreen
import br.com.salaopremiun.profissional.presentation.screens.SupportScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun SalaoPremiunProfessionalApp() {
    val context = LocalContext.current.applicationContext
    val sessionStore = remember { SessionStore(context) }
    val database = remember { ProfessionalDatabase.get(context) }
    val repository = remember {
        ProfessionalAppRepository(
            api = ApiClient.createProfessionalApi(sessionStore),
            sessionStore = sessionStore,
            cacheDao = database.cacheDao(),
        )
    }
    val viewModel: AppViewModel = viewModel(factory = AppViewModelFactory(repository))
    val state by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = bottomDestinations.any { it.route == currentRoute }
    val showChrome = currentRoute != AppDestination.Login.route
    val chromeTitle = chromeTitleForRoute(currentRoute)
    val chromeSubtitle = when (currentRoute) {
        AppDestination.Home.route -> "Bom dia, ${state.profile?.name ?: "profissional"}"
        AppDestination.Agenda.route -> "Hoje"
        else -> state.profile?.salonName ?: "SalaoPremiun"
    }

    ProfessionalAppBackground {
        LaunchedEffect(state.authenticated) {
            if (state.authenticated && currentRoute == AppDestination.Login.route) {
                navController.navigate(AppDestination.Home.route) {
                    popUpTo(AppDestination.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            if (state.authenticated) {
                runCatching {
                    if (FirebaseApp.getApps(context).isNotEmpty()) {
                        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                            if (token.isNotBlank()) viewModel.saveDeviceToken(token)
                        }
                    }
                }
            }
        }
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (showChrome) {
                    AppChromeHeader(title = chromeTitle, subtitle = chromeSubtitle)
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { destination ->
                            navController.navigate(destination.route) {
                                launchSingleTop = true
                                popUpTo(AppDestination.Home.route) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        },
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppDestination.Login.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(AppDestination.Login.route) {
                    LoginScreen(
                        state = state,
                        viewModel = viewModel,
                    )
                }
                composable(AppDestination.Home.route) {
                    DashboardScreen(
                        state = state,
                        onOpenAgenda = { navController.navigate(AppDestination.Agenda.route) },
                        onOpenCommands = { navController.navigate(AppDestination.Commands.route) },
                        onOpenNotifications = { navController.navigate(AppDestination.Notifications.route) },
                        onOpenProfile = { navController.navigate(AppDestination.Profile.route) },
                    )
                }
                composable(AppDestination.Clients.route) {
                    ClientsScreen(
                        state = state,
                        viewModel = viewModel,
                        onNewClient = { navController.navigate(AppDestination.ClientForm.route) },
                        onOpenClient = { id -> navController.navigate(AppDestination.ClientDetail.create(id)) },
                    )
                }
                composable(AppDestination.ClientForm.route) {
                    ClientFormScreen("Cliente", viewModel)
                }
                composable(AppDestination.ClientDetail.route) { entry ->
                    val id = entry.arguments?.getString("id").orEmpty()
                    ClientDetailScreen(
                        state = state,
                        viewModel = viewModel,
                        clientId = id,
                        onNewAppointment = { navController.navigate(AppDestination.NewAppointment.route) },
                        onNewCommand = { navController.navigate(AppDestination.NewCommand.route) },
                    )
                }
                composable(AppDestination.Agenda.route) {
                    AgendaScreen(
                        state = state,
                        viewModel = viewModel,
                        onNewAppointment = { navController.navigate(AppDestination.NewAppointment.route) },
                        onOpenAppointment = { id -> navController.navigate(AppDestination.AppointmentDetail.create(id)) },
                    )
                }
                composable(AppDestination.NewAppointment.route) {
                    NewAppointmentScreen(state, viewModel)
                }
                composable(AppDestination.AppointmentDetail.route) { entry ->
                    val id = entry.arguments?.getString("id").orEmpty()
                    AppointmentDetailScreen(
                        state = state,
                        viewModel = viewModel,
                        appointmentId = id,
                        onEdit = { navController.navigate(AppDestination.EditAppointment.create(id)) },
                        onOpenCommand = { commandId -> navController.navigate(AppDestination.CommandDetail.create(commandId)) },
                    )
                }
                composable(AppDestination.EditAppointment.route) { entry ->
                    EditAppointmentScreen(
                        state = state,
                        viewModel = viewModel,
                        appointmentId = entry.arguments?.getString("id").orEmpty(),
                    )
                }
                composable(AppDestination.Commands.route) {
                    CommandsScreen(
                        state = state,
                        viewModel = viewModel,
                        onNewCommand = { navController.navigate(AppDestination.NewCommand.route) },
                        onOpenCommand = { id -> navController.navigate(AppDestination.CommandDetail.create(id)) },
                    )
                }
                composable(AppDestination.NewCommand.route) {
                    NewCommandScreen(state = state, viewModel = viewModel)
                }
                composable(AppDestination.CommandDetail.route) { entry ->
                    CommandDetailScreen(
                        state = state,
                        viewModel = viewModel,
                        commandId = entry.arguments?.getString("id").orEmpty(),
                        onAddItem = { navController.navigate(AppDestination.AddCommandItem.route) },
                    )
                }
                composable(AppDestination.AddCommandItem.route) {
                    AddCommandItemScreen(state, viewModel)
                }
                composable(AppDestination.Commissions.route) {
                    CommissionsScreen(state = state, viewModel = viewModel)
                }
                composable(AppDestination.Notifications.route) {
                    NotificationsScreen(state = state, viewModel = viewModel)
                }
                composable(AppDestination.Profile.route) {
                    ProfileScreen(
                        state = state,
                        onDetails = { navController.navigate(AppDestination.ProfileDetails.route) },
                        onReviews = { navController.navigate(AppDestination.Reviews.route) },
                        onNotifications = { navController.navigate(AppDestination.Notifications.route) },
                        onSettings = { navController.navigate(AppDestination.Settings.route) },
                        onPassword = { navController.navigate(AppDestination.ChangePassword.route) },
                        onSupport = { navController.navigate(AppDestination.Support.route) },
                        onQuestions = { navController.navigate(AppDestination.Questions.route) },
                        onLegal = { navController.navigate(AppDestination.Legal.route) },
                        onPrivacy = { navController.navigate(AppDestination.Privacy.route) },
                        onInstall = { navController.navigate(AppDestination.Install.route) },
                        onOnboarding = { navController.navigate(AppDestination.Onboarding.route) },
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(AppDestination.Login.route)
                        },
                    )
                }
                composable(AppDestination.Settings.route) {
                    SettingsScreen(state = state, viewModel = viewModel)
                }
                composable(AppDestination.ChangePassword.route) {
                    ChangePasswordScreen(state = state, viewModel = viewModel)
                }
                composable(AppDestination.PasswordRecovery.route) {
                    PasswordRecoveryScreen()
                }
                composable(AppDestination.ProfileDetails.route) {
                    ProfileDetailsScreen(state = state)
                }
                composable(AppDestination.Reviews.route) {
                    ReviewsScreen()
                }
                composable(AppDestination.Support.route) {
                    SupportScreen()
                }
                composable(AppDestination.Questions.route) {
                    QuestionsScreen()
                }
                composable(AppDestination.Legal.route) {
                    LegalScreen()
                }
                composable(AppDestination.Privacy.route) {
                    PrivacyScreen()
                }
                composable(AppDestination.Install.route) {
                    InstallScreen()
                }
                composable(AppDestination.Onboarding.route) {
                    OnboardingScreen()
                }
            }
        }
    }
}

private fun chromeTitleForRoute(route: String?): String {
    return when (route) {
        AppDestination.Home.route -> "Início"
        AppDestination.Clients.route -> "Clientes"
        AppDestination.Agenda.route -> "Agenda"
        AppDestination.Commands.route -> "Comandas"
        AppDestination.Profile.route -> "Perfil"
        AppDestination.NewAppointment.route -> "Novo horário"
        AppDestination.Notifications.route -> "Notificações"
        AppDestination.Commissions.route -> "Comissões"
        AppDestination.Settings.route -> "Configurações"
        else -> "App Profissional"
    }
}
