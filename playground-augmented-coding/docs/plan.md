# plan.md - Address Normalizer (Kotlin)

## Normalization
- [x] normalize trims leading and trailing whitespace
- [x] normalize collapses multiple internal whitespaces into a single space
- [x] normalize converts tabs/newlines to single spaces
- [x] normalize keeps Korean characters and digits unchanged (no accidental removal)
- [x] normalize preserves hyphenated lot numbers like "123-4"
- [x] normalize normalizes "서울시" to "서울특별시"
- [x] normalize normalizes "부산시" to "부산광역시"
- [x] normalize does not change already-canonical forms like "서울특별시"
- [x] normalize is idempotent (normalize(normalize(x)) == normalize(x))

## Validation
- [x] validate rejects blank input with ValidationError
- [x] validate includes original input in error details
- [x] validate rejects input longer than 200 characters with ValidationError

# plan.md - Address Normalizer v2 (Kotlin)

## Normalization - Abbreviation Table
- [ ] normalize normalizes "경기" to "경기도"
- [ ] normalize normalizes "충남" to "충청남도"
- [ ] normalize normalizes "충북" to "충청북도"
- [ ] normalize normalizes "전남" to "전라남도"
- [ ] normalize normalizes "전북" to "전라북도"
- [ ] normalize normalizes "경남" to "경상남도"
- [ ] normalize normalizes "경북" to "경상북도"

## Normalization - Scope Safety
- [ ] normalize does not expand abbreviations when they are part of a longer word (e.g., "경기장" should not become "경기도장")
- [ ] normalize expands only when the abbreviation is a standalone token (token boundary)

## Normalization - Parentheses & Symbols
- [ ] normalize removes surrounding parentheses while preserving inside text (e.g., "서울특별시(광진구)" -> "서울특별시 광진구")
- [ ] normalize removes empty parentheses (e.g., "서울특별시()" -> "서울특별시")
- [ ] normalize collapses spaces created by parentheses removal

## Normalization - Pipeline & Refactoring Pressure
- [ ] normalize applies transformations via an explicit pipeline (list of steps) without changing behavior
- [ ] normalize keeps behavior identical for all previous v1 tests

## Property-like checks (deterministic)
- [ ] normalize is idempotent for inputs with random extra spaces and newlines (use a fixed set of samples)
