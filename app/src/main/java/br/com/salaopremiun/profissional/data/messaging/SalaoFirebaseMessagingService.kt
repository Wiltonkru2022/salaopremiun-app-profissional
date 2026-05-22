package br.com.salaopremiun.profissional.data.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SalaoFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // O token deve ser enviado para POST /api/profissional/device-token
        // após a sessão do profissional estar válida.
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // A exibição local será ligada na próxima etapa com canal de notificação.
    }
}
