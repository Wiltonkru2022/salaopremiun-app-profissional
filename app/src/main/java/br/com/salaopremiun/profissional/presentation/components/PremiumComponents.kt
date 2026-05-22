package br.com.salaopremiun.profissional.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.salaopremiun.profissional.domain.model.AppointmentPreview
import br.com.salaopremiun.profissional.domain.model.AppointmentStatus
import br.com.salaopremiun.profissional.domain.model.ClientSummary
import br.com.salaopremiun.profissional.domain.model.CommandStatus
import br.com.salaopremiun.profissional.domain.model.CommandSummary
import br.com.salaopremiun.profissional.presentation.navigation.AppDestination
import br.com.salaopremiun.profissional.presentation.navigation.bottomDestinations
import br.com.salaopremiun.profissional.ui.theme.GoldWeb

@Composable
fun ProfessionalAppBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Color(0xFFFFF7DF),
                    0.32f to Color(0xFFF5F5F5),
                    1f to Color(0xFFECEFF3),
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xCCF5F5F5)),
        ) {
            content()
        }
    }
}

@Composable
fun AppChromeHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.96f),
        shadowElevation = 8.dp,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0xFFFFF4D6),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFF8E6A2F),
                            modifier = Modifier.size(12.dp),
                        )
                        Text(
                            text = "Salão Premium Profissional",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF8E6A2F),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Text(
                    text = title,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF101828),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF667085),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = Color(0xFFF6F7F9),
                border = BorderStroke(1.dp, Color(0xFFE4E7EC)),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Spa,
                        contentDescription = null,
                        tint = Color(0xFF667085),
                    )
                }
            }
        }
    }
}

@Composable
fun AppHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color(0xFF101828),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF667085),
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(14.dp),
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, Color(0xCCE4E7EC)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.97f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Composable
fun HeroCard(
    professionalName: String,
    nextTime: String,
    todayCount: Int,
    monthCount: Int,
    onProfile: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF09090B),
        shadowElevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color.White.copy(alpha = 0.10f),
                    ) {
                        Text(
                            text = "Acesso ativo",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFE8A3),
                        )
                    }
                    Text(
                        text = professionalName,
                        modifier = Modifier.padding(top = 12.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                    )
                    Text(
                        text = "Seu dia no salão, pronto para atender, abrir comandas e seguir a agenda sem perder tempo.",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD4D4D8),
                    )
                }

                Surface(
                    onClick = onProfile,
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.White,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Abrir perfil",
                            tint = Color(0xFF09090B),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                HeroMetric("Próximo", nextTime, Modifier.weight(1f))
                HeroMetric("Hoje", todayCount.toString(), Modifier.weight(1f))
                HeroMetric("Mês", monthCount.toString(), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HeroMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.10f))
            .padding(10.dp),
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA1A1AA),
        )
        Text(
            text = value,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
fun ActionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(106.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE4E7EC)),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF8F3E7)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = GoldWeb, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF101828))
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF667085))
            }
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF09090B),
            contentColor = Color.White,
        ),
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF667085))
        },
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF101828),
            unfocusedBorderColor = Color(0xFFE4E7EC),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFFF6F7F9),
        ),
    )
}

@Composable
fun StatusBadge(
    text: String,
    color: StatusColor,
    modifier: Modifier = Modifier,
) {
    val colors = when (color) {
        StatusColor.Blue -> Color(0xFFE0F2FE) to Color(0xFF0369A1)
        StatusColor.Green -> Color(0xFFECFDF3) to Color(0xFF047857)
        StatusColor.Red -> Color(0xFFFEE2E2) to Color(0xFFB42318)
        StatusColor.Gold -> Color(0xFFFFF4D6) to Color(0xFFB66B1A)
        StatusColor.Neutral -> Color(0xFFF4F4F5) to Color(0xFF52525B)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = colors.first,
        border = BorderStroke(1.dp, colors.second.copy(alpha = 0.20f)),
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = colors.second,
        )
    }
}

enum class StatusColor {
    Blue,
    Green,
    Red,
    Gold,
    Neutral,
}

@Composable
fun AppointmentCard(
    appointment: AppointmentPreview,
    onOpen: () -> Unit,
) {
    Surface(
        onClick = onOpen,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        color = Color(0xB3F6F7F9),
        border = BorderStroke(1.dp, Color(0xFFE4E7EC)),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            appointment.timeRange.replace(" - ", " • "),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA1A1AA),
                        )
                    }
                    Text(
                        appointment.clientName,
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF101828),
                    )
                    Text(
                        appointment.serviceName,
                        modifier = Modifier.padding(top = 3.dp),
                        color = Color(0xFF667085),
                    )
                    Surface(
                        modifier = Modifier.padding(top = 9.dp),
                        shape = RoundedCornerShape(999.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFE4E7EC)),
                    ) {
                        Text(
                            text = appointment.professionalName,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF667085),
                        )
                    }
                }
                StatusBadge(
                    text = appointment.status.label,
                    color = when (appointment.status) {
                        AppointmentStatus.Confirmed -> StatusColor.Green
                        AppointmentStatus.InProgress -> StatusColor.Blue
                        AppointmentStatus.PendingConfirmation -> StatusColor.Gold
                        AppointmentStatus.WaitingPayment -> StatusColor.Gold
                    },
                )
            }
        }
    }
}

@Composable
fun ClientCard(
    client: ClientSummary,
    onOpen: () -> Unit,
) {
    PremiumCard {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(client.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("WhatsApp: ${client.phone}", color = Color(0xFF667085))
            Text("Última visita: ${client.lastVisit}", color = Color(0xFF667085))
            PrimaryButton(text = "Abrir cliente", onClick = onOpen)
        }
    }
}

@Composable
fun ComandaCard(
    command: CommandSummary,
    onOpen: () -> Unit,
) {
    PremiumCard {
        Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(command.clientName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                StatusBadge(
                    text = command.status.label,
                    color = when (command.status) {
                        CommandStatus.Open -> StatusColor.Gold
                        CommandStatus.SentToCashier -> StatusColor.Blue
                        CommandStatus.Closed -> StatusColor.Green
                        CommandStatus.Canceled -> StatusColor.Red
                    },
                )
            }
            Text("${command.itemCount} itens", color = Color(0xFF667085))
            Text(command.total.format(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            PrimaryButton(text = "Abrir comanda", onClick = onOpen)
        }
    }
}

@Composable
fun EmptyState(
    title: String,
    message: String,
) {
    PremiumCard {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(message, color = Color(0xFF667085))
        }
    }
}

@Composable
fun LoadingState() {
    PremiumCard {
        Text("Carregando dados com segurança...")
    }
}

@Composable
fun ErrorState(message: String) {
    PremiumCard {
        Text(message, color = Color(0xFFB42318), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CalendarView(
    selectedDay: Int,
    onDaySelected: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Maio de 2026", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (18..24).forEach { day ->
                val selected = day == selectedDay
                Surface(
                    onClick = { onDaySelected(day) },
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = if (selected) Color(0xFF09090B) else Color(0xFFF6F7F9),
                    border = BorderStroke(1.dp, if (selected) Color(0xFF09090B) else Color(0xFFE4E7EC)),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = day.toString(),
                                color = if (selected) Color.White else Color(0xFF101828),
                                fontWeight = FontWeight.Black,
                            )
                            Text(
                                text = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")[(day - 18) % 7],
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selected) Color.White.copy(alpha = 0.75f) else Color(0xFF667085),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    currentRoute: String?,
    onNavigate: (AppDestination) -> Unit,
) {
    val icons = mapOf(
        AppDestination.Home.route to Icons.Filled.Home,
        AppDestination.Clients.route to Icons.Filled.Groups,
        AppDestination.Agenda.route to Icons.Filled.CalendarMonth,
        AppDestination.Commands.route to Icons.AutoMirrored.Filled.ReceiptLong,
        AppDestination.Profile.route to Icons.Filled.Person,
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.94f),
        shadowElevation = 18.dp,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomDestinations.forEach { destination ->
                val selected = currentRoute == destination.route
                val itemColor = if (selected) Color(0xFF09090B) else Color.Transparent
                val contentColor = if (selected) Color.White else Color(0xFF667085)

                Surface(
                    onClick = { onNavigate(destination) },
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = itemColor,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = icons[destination.route] ?: Icons.Filled.Home,
                            contentDescription = destination.label,
                            tint = contentColor,
                            modifier = Modifier.size(19.dp),
                        )
                        Text(
                            text = destination.label,
                            modifier = Modifier.padding(top = 4.dp),
                            maxLines = 1,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor,
                        )
                    }
                }
            }
        }
    }
}
