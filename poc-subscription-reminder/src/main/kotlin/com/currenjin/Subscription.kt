package com.currenjin

import java.time.LocalDate

data class Subscription(
    val id: Long,
    val name: String,
    val amount: Int?,
    val cycle: BillingCycle,
    val firstBillingDate: LocalDate,
    val reminderOffsets: List<Int> = listOf(7, 3, 1, 0),
)
