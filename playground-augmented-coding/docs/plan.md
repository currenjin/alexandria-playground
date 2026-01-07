# plan.md - Address Normalizer (Kotlin)

## Normalization
- [ ] normalize trims leading and trailing whitespace
- [ ] normalize collapses multiple internal whitespaces into a single space
- [ ] normalize converts tabs/newlines to single spaces
- [ ] normalize keeps Korean characters and digits unchanged (no accidental removal)
- [ ] normalize preserves hyphenated lot numbers like "123-4"
- [ ] normalize normalizes "서울시" to "서울특별시"
- [ ] normalize normalizes "부산시" to "부산광역시"
- [ ] normalize does not change already-canonical forms like "서울특별시"
- [ ] normalize is idempotent (normalize(normalize(x)) == normalize(x))

## Validation
- [ ] validate rejects blank input with ValidationError
- [ ] validate includes original input in error details
- [ ] validate rejects input longer than 200 characters with ValidationError
