package com.currenjin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ReminderMessageFormatterTest {
    @Test
    fun shouldFormatMessageDifferentlyWhenAmountExists() {
        val dueDate = LocalDate.of(2026, 1, 15)

        val withAmount = ReminderMessageFormatter.format(
            name = "Netflix",
            amount = 14000,
            dueDate = dueDate,
        )
        val withoutAmount = ReminderMessageFormatter.format(
            name = "Netflix",
            amount = null,
            dueDate = dueDate,
        )

        assertThat(withAmount)
            .isEqualTo("Netflix subscription is due on 2026-01-15. Amount: 14000")
        assertThat(withoutAmount)
            .isEqualTo("Netflix subscription is due on 2026-01-15.")
    }
}
