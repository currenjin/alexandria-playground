package com.currenjin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ReminderExtractorTest {
    @Test
    fun shouldCreateReminderWhenDueDateMatchesOffset() {
        val today = LocalDate.of(2026, 1, 10)
        val dueDate = today.plusDays(3)

        val reminder = ReminderExtractor.extract(
            subscriptionId = 1L,
            dueDate = dueDate,
            today = today,
            offsets = listOf(7, 3, 1, 0),
        )

        assertThat(reminder).isNotNull
        assertThat(reminder!!.dDay).isEqualTo(3)
        assertThat(reminder.dueDate).isEqualTo(dueDate)
    }
}
