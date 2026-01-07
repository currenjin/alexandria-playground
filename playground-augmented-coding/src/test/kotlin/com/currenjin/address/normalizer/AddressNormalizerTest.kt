package com.currenjin.address.normalizer

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class AddressNormalizerTest {
    private val normalizer = AddressNormalizer()

    @Test
    fun shouldTrimLeadingAndTrailingWhitespace() {
        val result = normalizer.normalize("  서울특별시 강남구  ")

        assertThat(result).isEqualTo("서울특별시 강남구")
    }

    @Test
    fun shouldCollapseMultipleInternalSpacesToSingleSpace() {
        val result = normalizer.normalize("서울특별시  강남구   역삼동")

        assertThat(result).isEqualTo("서울특별시 강남구 역삼동")
    }

    @Test
    fun shouldConvertTabsAndNewlinesToSingleSpaces() {
        val result = normalizer.normalize("서울특별시\t강남구\n역삼동")

        assertThat(result).isEqualTo("서울특별시 강남구 역삼동")
    }

    @Test
    fun shouldKeepKoreanCharactersAndDigitsUnchanged() {
        val result = normalizer.normalize("서울특별시 123 강남구 7길")

        assertThat(result).isEqualTo("서울특별시 123 강남구 7길")
    }

    @Test
    fun shouldPreserveHyphenatedLotNumbers() {
        val result = normalizer.normalize("서울특별시 강남구 123-4")

        assertThat(result).isEqualTo("서울특별시 강남구 123-4")
    }

    @Test
    fun shouldNormalizeSeoulCityToSeoulMetropolitanCity() {
        val result = normalizer.normalize("서울시 강남구")

        assertThat(result).isEqualTo("서울특별시 강남구")
    }

    @Test
    fun shouldNormalizeBusanCityToBusanMetropolitanCity() {
        val result = normalizer.normalize("부산시 해운대구")

        assertThat(result).isEqualTo("부산광역시 해운대구")
    }

    @Test
    fun shouldNotChangeAlreadyCanonicalSeoulMetropolitanCity() {
        val result = normalizer.normalize("서울특별시 강남구")

        assertThat(result).isEqualTo("서울특별시 강남구")
    }

    @Test
    fun shouldBeIdempotent() {
        val once = normalizer.normalize("  서울시  강남구 ")
        val twice = normalizer.normalize(once)

        assertThat(twice).isEqualTo(once)
    }

    @Test
    fun shouldRejectBlankInputWithValidationError() {
        assertThatThrownBy { normalizer.validate("   ") }
            .isInstanceOfSatisfying(ValidationError::class.java) { error ->
                assertThat(error.input).isEqualTo("   ")
            }
    }
}
