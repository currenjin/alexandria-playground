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
    fun shouldNormalizeGyeonggiToGyeonggiProvince() {
        val result = normalizer.normalize("경기 수원시")

        assertThat(result).isEqualTo("경기도 수원시")
    }

    @Test
    fun shouldNormalizeChungnamToChungcheongnamProvince() {
        val result = normalizer.normalize("충남 천안시")

        assertThat(result).isEqualTo("충청남도 천안시")
    }

    @Test
    fun shouldNormalizeChungbukToChungcheongbukProvince() {
        val result = normalizer.normalize("충북 청주시")

        assertThat(result).isEqualTo("충청북도 청주시")
    }

    @Test
    fun shouldNormalizeJeonnamToJeollanamProvince() {
        val result = normalizer.normalize("전남 목포시")

        assertThat(result).isEqualTo("전라남도 목포시")
    }

    @Test
    fun shouldNormalizeJeonbukToJeollabukProvince() {
        val result = normalizer.normalize("전북 전주시")

        assertThat(result).isEqualTo("전라북도 전주시")
    }

    @Test
    fun shouldNormalizeGyeongnamToGyeongsangnamProvince() {
        val result = normalizer.normalize("경남 창원시")

        assertThat(result).isEqualTo("경상남도 창원시")
    }

    @Test
    fun shouldNormalizeGyeongbukToGyeongsangbukProvince() {
        val result = normalizer.normalize("경북 포항시")

        assertThat(result).isEqualTo("경상북도 포항시")
    }

    @Test
    fun shouldNotExpandAbbreviationInsideLongerWord() {
        val result = normalizer.normalize("경기장")

        assertThat(result).isEqualTo("경기장")
    }

    @Test
    fun shouldExpandAbbreviationOnlyWhenStandaloneToken() {
        val result = normalizer.normalize("서울시 경기 수원시 경기장")

        assertThat(result).isEqualTo("서울특별시 경기도 수원시 경기장")
    }

    @Test
    fun shouldRemoveSurroundingParenthesesWhilePreservingText() {
        val result = normalizer.normalize("서울특별시(광진구)")

        assertThat(result).isEqualTo("서울특별시 광진구")
    }

    @Test
    fun shouldRemoveEmptyParentheses() {
        val result = normalizer.normalize("서울특별시()")

        assertThat(result).isEqualTo("서울특별시")
    }

    @Test
    fun shouldCollapseSpacesCreatedByParenthesesRemoval() {
        val result = normalizer.normalize("서울특별시 ( 광진구 )")

        assertThat(result).isEqualTo("서울특별시 광진구")
    }

    @Test
    fun shouldKeepBehaviorIdenticalForPreviousV1Samples() {
        val samples = listOf(
            "  서울특별시 강남구  " to "서울특별시 강남구",
            "서울특별시  강남구   역삼동" to "서울특별시 강남구 역삼동",
            "서울특별시\t강남구\n역삼동" to "서울특별시 강남구 역삼동",
            "서울특별시 123 강남구 7길" to "서울특별시 123 강남구 7길",
            "서울특별시 강남구 123-4" to "서울특별시 강남구 123-4",
            "서울시 강남구" to "서울특별시 강남구",
            "부산시 해운대구" to "부산광역시 해운대구",
            "서울특별시 강남구" to "서울특별시 강남구",
        )

        samples.forEach { (input, expected) ->
            assertThat(normalizer.normalize(input)).isEqualTo(expected)
        }
    }

    @Test
    fun shouldBeIdempotentForFixedSamplesWithExtraWhitespaceAndNewlines() {
        val samples = listOf(
            "  서울시  강남구  ",
            "부산시\t해운대구\n",
            "경기  수원시\n영통구",
            "서울특별시 ( 광진구 )",
            "충남\t천안시  ",
        )

        samples.forEach { input ->
            val once = normalizer.normalize(input)
            val twice = normalizer.normalize(once)

            assertThat(twice).isEqualTo(once)
        }
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
    fun shouldReturnNormalizedValueAndAppliedRules() {
        val report = normalizer.normalizeWithReport("  서울특별시  ")

        assertThat(report.value).isEqualTo("서울특별시")
        assertThat(report.appliedRules).containsExactly(NormalizationRule.TRIM)
    }

    @Test
    fun shouldReturnEmptyAppliedRulesWhenInputIsAlreadyNormalized() {
        val report = normalizer.normalizeWithReport("서울특별시 강남구")

        assertThat(report.value).isEqualTo("서울특별시 강남구")
        assertThat(report.appliedRules).isEmpty()
    }

    @Test
    fun shouldIncludeWhitespaceCollapseWhenMultipleSpacesAreCollapsed() {
        val report = normalizer.normalizeWithReport("서울특별시  강남구")

        assertThat(report.value).isEqualTo("서울특별시 강남구")
        assertThat(report.appliedRules).containsExactly(NormalizationRule.WHITESPACE_COLLAPSE)
    }

    @Test
    fun shouldIncludeNewlineToSpaceWhenTabsOrNewlinesAreConverted() {
        val report = normalizer.normalizeWithReport("서울특별시\t강남구\n역삼동")

        assertThat(report.value).isEqualTo("서울특별시 강남구 역삼동")
        assertThat(report.appliedRules).containsExactly(NormalizationRule.NEWLINE_TO_SPACE)
    }

    @Test
    fun shouldIncludeAbbreviationExpandWhenSeoulIsExpanded() {
        val report = normalizer.normalizeWithReport("서울시 강남구")

        assertThat(report.value).isEqualTo("서울특별시 강남구")
        assertThat(report.appliedRules).containsExactly(NormalizationRule.ABBR_EXPAND)
    }

    @Test
    fun shouldRejectBlankInputWithValidationError() {
        assertThatThrownBy { normalizer.validate("   ") }
            .isInstanceOfSatisfying(ValidationError::class.java) { error ->
                assertThat(error.input).isEqualTo("   ")
            }
    }

    @Test
    fun shouldRejectInputLongerThan200CharactersWithValidationError() {
        val longInput = "a".repeat(201)

        assertThatThrownBy { normalizer.validate(longInput) }
            .isInstanceOf(ValidationError::class.java)
    }
}
