package br.com.salaopremiun.profissional.presentation.navigation

sealed class AppDestination(
    val route: String,
    val label: String,
) {
    data object Login : AppDestination("login", "Login")
    data object Home : AppDestination("inicio", "Início")
    data object Clients : AppDestination("clientes", "Clientes")
    data object ClientForm : AppDestination("clientes/formulario", "Cliente")
    data object Agenda : AppDestination("agenda", "Agenda")
    data object NewAppointment : AppDestination("agenda/novo", "Novo")
    data object AppointmentDetail : AppDestination("agenda/detalhe", "Detalhe")
    data object EditAppointment : AppDestination("agenda/editar", "Editar")
    data object Commands : AppDestination("comandas", "Comandas")
    data object CommandDetail : AppDestination("comandas/detalhe", "Comanda")
    data object AddCommandItem : AppDestination("comandas/item", "Adicionar")
    data object Commissions : AppDestination("comissoes", "Comissões")
    data object Notifications : AppDestination("notificacoes", "Notificações")
    data object Profile : AppDestination("perfil", "Perfil")
    data object Settings : AppDestination("configuracoes", "Configurações")
    data object ChangePassword : AppDestination("alterar-senha", "Senha")
    data object Support : AppDestination("suporte", "Suporte")
    data object Legal : AppDestination("termos", "Termos")
}

val bottomDestinations = listOf(
    AppDestination.Home,
    AppDestination.Clients,
    AppDestination.Agenda,
    AppDestination.Commands,
    AppDestination.Profile,
)
