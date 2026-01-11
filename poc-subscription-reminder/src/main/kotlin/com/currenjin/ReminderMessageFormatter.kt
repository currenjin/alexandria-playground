package com.currenjin

import java.time.LocalDate

object ReminderMessageFormatter {
    fun format(name: String, amount: Int?, dueDate: LocalDate): String {
        return if (amount == null) {
            "$name subscription is due on $dueDate."
        } else {
            "$name subscription is due on $dueDate. Amount: $amount"
        }
    }
}
