package com.currenjin.address.normalizer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddressNormalizerTest {
    @Test
    fun shouldTrimLeadingAndTrailingWhitespace() {
        val normalizer = AddressNormalizer()

        val result = normalizer.normalize("  서울특별시 강남구  ")

        assertThat(result).isEqualTo("서울특별시 강남구")
    }

    @Test
    fun shouldCollapseMultipleInternalSpacesToSingleSpace() {
        val normalizer = AddressNormalizer()

        val result = normalizer.normalize("서울특별시  강남구   역삼동")

        assertThat(result).isEqualTo("서울특별시 강남구 역삼동")
    }

    @Test
    fun shouldConvertTabsAndNewlinesToSingleSpaces() {
        val normalizer = AddressNormalizer()

        val result = normalizer.normalize("서울특별시\t강남구\n역삼동")

        assertThat(result).isEqualTo("서울특별시 강남구 역삼동")
    }

    @Test
    fun shouldKeepKoreanCharactersAndDigitsUnchanged() {
        val normalizer = AddressNormalizer()

        val result = normalizer.normalize("서울특별시 123 강남구 7길")

        assertThat(result).isEqualTo("서울특별시 123 강남구 7길")
    }

    @Test
    fun shouldPreserveHyphenatedLotNumbers() {
        val normalizer = AddressNormalizer()

        val result = normalizer.normalize("서울특별시 강남구 123-4")

        assertThat(result).isEqualTo("서울특별시 강남구 123-4")
    }
}
