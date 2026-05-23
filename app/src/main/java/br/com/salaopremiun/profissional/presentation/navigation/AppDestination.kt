package br.com.salaopremiun.profissional.presentation.navigation

sealed class AppDestination(
    val route: String,
    val label: String,
) {
    data object Login : AppDestination("login", "Login")
    data object Home : AppDestination("inicio", "Início")
    data object Clients : AppDestination("clientes", "Clientes")
    data object ClientForm : AppDestination("clientes/formulario", "Cliente")
    data object ClientDetail : AppDestination("clientes/detalhe/{id}", "Cliente") {
        fun create(id: String) = "clientes/detalhe/$id"
    }
    data object Agenda : AppDestination("agenda", "Agenda")
    data object NewAppointment : AppDestination("agenda/novo", "Novo")
    data object AppointmentDetail : AppDestination("agenda/detalhe/{id}", "Detalhe") {
        fun create(id: String) = "agenda/detalhe/$id"
    }
    data object EditAppointment : AppDestination("agenda/editar/{id}", "Editar") {
        fun create(id: String) = "agenda/editar/$id"
    }
    data object Commands : AppDestination("comandas", "Comandas")
    data object NewCommand : AppDestination("comandas/nova", "Nova")
    data object CommandDetail : AppDestination("comandas/detalhe/{id}", "Comanda") {
        fun create(id: String) = "comandas/detalhe/$id"
    }
    data object AddCommandItem : AppDestination("comandas/item", "Adicionar")
    data object Commissions : AppDestination("comissoes", "Comissões")
    data object Notifications : AppDestination("notificacoes", "Notificações")
    data object Profile : AppDestination("perfil", "Perfil")
    data object Settings : AppDestination("configuracoes", "Configurações")
    data object ChangePassword : AppDestination("alterar-senha", "Senha")
    data object PasswordRecovery : AppDestination("recuperar-senha", "Recuperar")
    data object ProfileDetails : AppDestination("perfil/detalhes", "Detalhes")
    data object Reviews : AppDestination("avaliacoes", "Avaliações")
    data object Support : AppDestination("suporte", "Suporte")
    data object Questions : AppDestination("duvidas", "Dúvidas")
    data object Legal : AppDestination("termos", "Termos")
    data object Privacy : AppDestination("privacidade", "Privacidade")
    data object Install : AppDestination("instalar", "Instalar")
    data object Onboarding : AppDestination("onboarding", "Onboarding")
}

val bottomDestinations = listOf(
    AppDestination.Home,
    AppDestination.Clients,
    AppDestination.Agenda,
    AppDestination.Commands,
    AppDestination.Profile,
)
