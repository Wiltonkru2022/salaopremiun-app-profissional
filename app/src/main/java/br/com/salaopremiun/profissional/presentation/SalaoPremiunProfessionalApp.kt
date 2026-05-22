package br.com.salaopremiun.profissional.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import br.com.salaopremiun.profissional.presentation.screens.ClientFormScreen
import br.com.salaopremiun.profissional.presentation.screens.ClientsScreen
import br.com.salaopremiun.profissional.presentation.screens.CommandDetailScreen
import br.com.salaopremiun.profissional.presentation.screens.CommandsScreen
import br.com.salaopremiun.profissional.presentation.screens.CommissionsScreen
import br.com.salaopremiun.profissional.presentation.screens.DashboardScreen
import br.com.salaopremiun.profissional.presentation.screens.EditAppointmentScreen
import br.com.salaopremiun.profissional.presentation.screens.LegalScreen
import br.com.salaopremiun.profissional.presentation.screens.LoginScreen
import br.com.salaopremiun.profissional.presentation.screens.NewAppointmentScreen
import br.com.salaopremiun.profissional.presentation.screens.NotificationsScreen
import br.com.salaopremiun.profissional.presentation.screens.ProfileScreen
import br.com.salaopremiun.profissional.presentation.screens.SettingsScreen
import br.com.salaopremiun.profissional.presentation.screens.SupportScreen

@Composable
fun SalaoPremiunProfessionalApp() {
    val context = LocalContext.current.applicationContext
    val sessionStore = remember { SessionStore(context) }
    val repository = remember {
        ProfessionalAppRepository(
            api = ApiClient.createProfessionalApi(sessionStore),
            sessionStore = sessionStore,
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
                startDestination = AppDestination.Home.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(AppDestination.Login.route) {
                    LoginScreen(
                        viewModel = viewModel,
                        onLoggedIn = { navController.navigate(AppDestination.Home.route) },
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
                        onOpenClient = { navController.navigate(AppDestination.ClientForm.route) },
                    )
                }
                composable(AppDestination.ClientForm.route) {
                    ClientFormScreen("Cliente")
                }
                composable(AppDestination.Agenda.route) {
                    AgendaScreen(
                        state = state,
                        viewModel = viewModel,
                        onNewAppointment = { navController.navigate(AppDestination.NewAppointment.route) },
                        onOpenAppointment = { navController.navigate(AppDestination.AppointmentDetail.route) },
                    )
                }
                composable(AppDestination.NewAppointment.route) {
                    NewAppointmentScreen()
                }
                composable(AppDestination.AppointmentDetail.route) {
                    AppointmentDetailScreen()
                }
                composable(AppDestination.EditAppointment.route) {
                    EditAppointmentScreen()
                }
                composable(AppDestination.Commands.route) {
                    CommandsScreen(
                        state = state,
                        viewModel = viewModel,
                        onOpenCommand = { navController.navigate(AppDestination.CommandDetail.route) },
                    )
                }
                composable(AppDestination.CommandDetail.route) {
                    CommandDetailScreen(
                        onAddItem = { navController.navigate(AppDestination.AddCommandItem.route) },
                    )
                }
                composable(AppDestination.AddCommandItem.route) {
                    AddCommandItemScreen()
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
                        onSettings = { navController.navigate(AppDestination.Settings.route) },
                        onPassword = { navController.navigate(AppDestination.ChangePassword.route) },
                        onSupport = { navController.navigate(AppDestination.Support.route) },
                        onLegal = { navController.navigate(AppDestination.Legal.route) },
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(AppDestination.Login.route)
                        },
                    )
                }
                composable(AppDestination.Settings.route) {
                    SettingsScreen()
                }
                composable(AppDestination.ChangePassword.route) {
                    ChangePasswordScreen()
                }
                composable(AppDestination.Support.route) {
                    SupportScreen()
                }
                composable(AppDestination.Legal.route) {
                    LegalScreen()
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
