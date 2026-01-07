package com.currenjin.address.normalizer

class AddressNormalizer {
    fun normalize(raw: String): String {
        return raw
            .trim()
            .replace(Regex("\\s+"), " ")
    }

    fun validate(raw: String) {
        TODO("Not yet Implemented")
    }
}
