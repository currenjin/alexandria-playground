package com.currenjin

import java.time.LocalDate

fun main(args: Array<String>) {
    val today = if (args.isNotEmpty()) {
        LocalDate.parse(args[0])
    } else {
        LocalDate.now()
    }

    val subscriptions = listOf(
        Subscription(
            id = 1L,
            name = "Netflix",
            amount = 14000,
            cycle = BillingCycle.Monthly(dayOfMonth = 15),
            firstBillingDate = LocalDate.of(2026, 1, 15),
        ),
        Subscription(
            id = 2L,
            name = "YouTube Premium",
            amount = 10900,
            cycle = BillingCycle.Monthly(dayOfMonth = 7),
            firstBillingDate = LocalDate.of(2026, 1, 7),
        ),
        Subscription(
            id = 3L,
            name = "Domain Renewal",
            amount = null,
            cycle = BillingCycle.Yearly(month = 6, dayOfMonth = 10),
            firstBillingDate = LocalDate.of(2020, 6, 10),
        ),
        Subscription(
            id = 4L,
            name = "Gym Pass",
            amount = 55000,
            cycle = BillingCycle.EveryNDays(days = 30),
            firstBillingDate = LocalDate.of(2026, 1, 1),
        ),
    )

    val reminders = subscriptions.mapNotNull { subscription ->
        val dueDate = subscription.cycle.nextBillingDate(subscription.firstBillingDate, today)
        ReminderExtractor.extract(
            subscriptionId = subscription.id,
            dueDate = dueDate,
            today = today,
            offsets = subscription.reminderOffsets,
        )?.let { candidate ->
            val message = ReminderMessageFormatter.format(
                name = subscription.name,
                amount = subscription.amount,
                dueDate = candidate.dueDate,
            )
            "D-${candidate.dDay}: $message"
        }
    }

    if (reminders.isEmpty()) {
        println("No reminders for $today.")
    } else {
        reminders.forEach(::println)
    }
}
