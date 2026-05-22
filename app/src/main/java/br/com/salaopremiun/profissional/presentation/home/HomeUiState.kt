package br.com.salaopremiun.profissional.presentation.home

import br.com.salaopremiun.profissional.domain.model.ProfessionalDashboard

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Content(val dashboard: ProfessionalDashboard) : HomeUiState
}
