package com.currenjin

import java.time.LocalDate
import java.time.YearMonth

sealed interface BillingCycle {
    fun nextBillingDate(firstBillingDate: LocalDate, today: LocalDate): LocalDate

    data class Monthly(val dayOfMonth: Int) : BillingCycle {
        override fun nextBillingDate(firstBillingDate: LocalDate, today: LocalDate): LocalDate {
            val thisMonth = YearMonth.from(today)
            val candidate = dateInMonth(thisMonth, dayOfMonth)
            if (!candidate.isBefore(today)) {
                return candidate
            }

            val nextMonth = thisMonth.plusMonths(1)
            return dateInMonth(nextMonth, dayOfMonth)
        }

        private fun dateInMonth(yearMonth: YearMonth, dayOfMonth: Int): LocalDate {
            val clampedDay = minOf(dayOfMonth, yearMonth.lengthOfMonth())
            return yearMonth.atDay(clampedDay)
        }
    }
}
