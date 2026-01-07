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
