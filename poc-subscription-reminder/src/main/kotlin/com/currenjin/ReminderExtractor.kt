package com.currenjin

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class ReminderCandidate(
    val subscriptionId: Long,
    val dueDate: LocalDate,
    val dDay: Int,
    val message: String,
)

object ReminderExtractor {
    fun extract(
        subscriptionId: Long,
        dueDate: LocalDate,
        today: LocalDate,
        offsets: List<Int>,
    ): ReminderCandidate? {
        val dDay = ChronoUnit.DAYS.between(today, dueDate).toInt()
        if (!offsets.contains(dDay)) {
            return null
        }

        return ReminderCandidate(subscriptionId, dueDate, dDay, message = "")
    }
}
