package com.currenjin.saga.core

class SagaOrchestrator<T>(
    private val steps: List<SagaStep<T>>,
    private val retryPolicy: RetryPolicy? = null,
) {
    private val completedSteps = mutableListOf<SagaStep<T>>()
    val log = mutableListOf<String>()

    fun execute(context: T): SagaResult {
        for (step in steps) {
            val result = executeWithRetry(step, context)

            when (result) {
                is SagaResult.Success -> {
                    completedSteps.add(step)
                    log.add("[${step.name}] 성공: ${result.message}")
                }
                is SagaResult.Failure -> {
                    log.add("[${step.name}] 실패: ${result.reason}")
                    compensate(context)
                    return result
                }
            }
        }
        return SagaResult.Success("모든 스텝 완료")
    }

    private fun executeWithRetry(step: SagaStep<T>, context: T): SagaResult {
        val policy = retryPolicy ?: return step.execute(context)

        var lastResult: SagaResult = step.execute(context)
        var attempt = 1

        while (lastResult is SagaResult.Failure && lastResult.retryable && attempt < policy.maxRetries) {
            log.add("[${step.name}] 재시도 ${attempt}/${policy.maxRetries - 1}")
            Thread.sleep(policy.delayMs)
            lastResult = step.execute(context)
            attempt++
        }

        return lastResult
    }

    private fun compensate(context: T) {
        log.add("보상 트랜잭션 시작 (${completedSteps.size}개 스텝)")
        for (step in completedSteps.reversed()) {
            val result = step.compensate(context)
            when (result) {
                is SagaResult.Success -> log.add("[${step.name}] 보상 완료: ${result.message}")
                is SagaResult.Failure -> log.add("[${step.name}] 보상 실패: ${result.reason}")
            }
        }
    }
}
