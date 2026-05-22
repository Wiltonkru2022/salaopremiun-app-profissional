package br.com.salaopremiun.profissional.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.salaopremiun.profissional.data.repository.ProfessionalAppRepository

class AppViewModelFactory(
    private val repository: ProfessionalAppRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(repository) as T
        }
        error("ViewModel não suportada: ${modelClass.name}")
    }
}
