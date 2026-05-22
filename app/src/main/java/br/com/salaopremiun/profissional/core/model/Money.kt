package br.com.salaopremiun.profissional.core.model

import java.text.NumberFormat
import java.util.Locale

data class Money(val cents: Long) {
    fun format(locale: Locale = Locale.forLanguageTag("pt-BR")): String {
        return NumberFormat.getCurrencyInstance(locale).format(cents / 100.0)
    }
}
