package com.currenjin.address.normalizer

class AddressNormalizer {
    fun normalize(raw: String): String {
        val pipeline = listOf<(String) -> String>(
            { input -> input.replace("(", " ").replace(")", " ") },
            { input -> input.trim().replace(Regex("\\s+"), " ") },
            { input -> expandAbbreviations(input) },
        )

        return pipeline.fold(raw) { value, step -> step(value) }
    }

    fun normalizeWithReport(raw: String): NormalizationReport {
        val appliedRules = mutableListOf<NormalizationRule>()
        if (raw != raw.trim()) {
            appliedRules.add(NormalizationRule.TRIM)
        }
        if (raw.trim().contains("  ")) {
            appliedRules.add(NormalizationRule.WHITESPACE_COLLAPSE)
        }

        val value = normalize(raw)
        return NormalizationReport(value, appliedRules)
    }

    private fun expandAbbreviations(input: String): String {
        if (input.isEmpty()) {
            return input
        }

        val replacements = mapOf(
            "서울시" to "서울특별시",
            "부산시" to "부산광역시",
            "경기" to "경기도",
            "충남" to "충청남도",
            "충북" to "충청북도",
            "전남" to "전라남도",
            "전북" to "전라북도",
            "경남" to "경상남도",
            "경북" to "경상북도",
        )

        return input
            .split(" ")
            .joinToString(" ") { token -> replacements[token] ?: token }
    }

    @Suppress("UNUSED_PARAMETER")
    fun validate(raw: String) {
        if (raw.isBlank()) {
            throw ValidationError(raw)
        }
        if (raw.length > 200) {
            throw ValidationError(raw)
        }
    }
}
