package com.currenjin.address.normalizer

class AddressNormalizer {
    fun normalize(raw: String): String {
        return raw
            .trim()
            .replace(Regex("\\s+"), " ")
            .replace("서울시", "서울특별시")
            .replace("부산시", "부산광역시")
            .replace("경기", "경기도")
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
