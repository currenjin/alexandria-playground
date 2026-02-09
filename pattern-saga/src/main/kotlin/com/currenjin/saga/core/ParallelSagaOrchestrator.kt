package com.currenjin.saga.core

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

data class StepGroup<T>(
    val steps: List<SagaStep<T>>,
)

class ParallelSagaOrchestrator<T>(
    private val groups: List<StepGroup<T>>,
) {
    private val completedGroups = mutableListOf<StepGroup<T>>()
    val log = mutableListOf<String>()

    fun execute(context: T): SagaResult {
        for (group in groups) {
            val results = executeGroup(group, context)
            val failure = results.entries.firstOrNull { it.value is SagaResult.Failure }

            if (failure != null) {
                val failureResult = failure.value as SagaResult.Failure
                log.add("[${failure.key}] 실패: ${failureResult.reason}")

                val succeededSteps = results.filter { it.value is SagaResult.Success }.keys
                compensateSteps(succeededSteps.mapNotNull { name -> group.steps.find { it.name == name } }, context)
                compensateCompletedGroups(context)

                return failureResult
            }

            for ((stepName, result) in results) {
                if (result is SagaResult.Success) {
                    log.add("[$stepName] 성공: ${result.message}")
                }
            }
            completedGroups.add(group)
        }
        return SagaResult.Success("모든 스텝 완료")
    }

    private fun executeGroup(group: StepGroup<T>, context: T): Map<String, SagaResult> {
        if (group.steps.size == 1) {
            val step = group.steps.first()
            return mapOf(step.name to step.execute(context))
        }

        return runBlocking {
            val deferred = group.steps.map { step ->
                async { step.name to step.execute(context) }
            }
            deferred.awaitAll().toMap()
        }
    }

    private fun compensateSteps(steps: List<SagaStep<T>>, context: T) {
        for (step in steps.reversed()) {
            val result = step.compensate(context)
            when (result) {
                is SagaResult.Success -> log.add("[${step.name}] 보상 완료: ${result.message}")
                is SagaResult.Failure -> log.add("[${step.name}] 보상 실패: ${result.reason}")
            }
        }
    }

    private fun compensateCompletedGroups(context: T) {
        log.add("보상 트랜잭션 시작 (${completedGroups.size}개 그룹)")
        for (group in completedGroups.reversed()) {
            compensateSteps(group.steps, context)
        }
    }
}
