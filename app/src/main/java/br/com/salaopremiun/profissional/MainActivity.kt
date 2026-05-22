package br.com.salaopremiun.profissional

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.salaopremiun.profissional.presentation.SalaoPremiunProfessionalApp
import br.com.salaopremiun.profissional.ui.theme.SalaoPremiunProfissionalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SalaoPremiunProfissionalTheme {
                SalaoPremiunProfessionalApp()
            }
        }
    }
}
