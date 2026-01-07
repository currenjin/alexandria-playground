package com.currenjin.address.normalizer

class AddressNormalizer {
    fun normalize(raw: String): String {
        return raw
            .trim()
            .replace(Regex("\\s+"), " ")
            .replace("서울시", "서울특별시")
    }

    fun validate(raw: String) {
        TODO("Not yet Implemented")
    }
}
