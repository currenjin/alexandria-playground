package com.currenjin.address.normalizer

class AddressNormalizer {
    fun normalize(raw: String): String {
        val normalizedWhitespace = raw
            .trim()
            .replace(Regex("\\s+"), " ")
        if (normalizedWhitespace.isEmpty()) {
            return normalizedWhitespace
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

        return normalizedWhitespace
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
