package com.currenjin.address.normalizer

class AddressNormalizer {
    fun normalize(raw: String): String {
        return raw
            .trim()
            .replace(Regex(" +"), " ")
    }

    fun validate(raw: String) {
        TODO("Not yet Implemented")
    }
}
