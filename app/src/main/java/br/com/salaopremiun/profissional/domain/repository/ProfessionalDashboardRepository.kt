package br.com.salaopremiun.profissional.domain.repository

import br.com.salaopremiun.profissional.domain.model.ProfessionalDashboard

interface ProfessionalDashboardRepository {
    fun getDashboard(): ProfessionalDashboard
}
