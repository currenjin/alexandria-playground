package com.currenjin.address.normalizer

data class NormalizationReport(
    val value: String,
    val appliedRules: List<NormalizationRule>,
)
