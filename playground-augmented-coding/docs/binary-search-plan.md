# plan.md - Binary Search (Kotlin)

## Result 타입 정의
- [x] BinarySearchResult sealed class 정의 (Found, NotFound)
- [x] Found는 index 값을 포함
- [x] NotFound는 삽입 위치(insertionPoint)를 포함

## 빈 배열과 단일 요소
- [x] 빈 배열에서 검색 -> NotFound 반환
- [x] 단일 요소 배열에서 해당 값 검색 -> Found(0) 반환
- [x] 단일 요소 배열에서 없는 값 검색 -> NotFound 반환

## 여러 요소 배열
- [x] 배열의 첫 번째 요소 검색 -> Found(0) 반환
- [x] 배열의 마지막 요소 검색 -> Found(lastIndex) 반환
- [x] 배열의 중간 요소 검색 -> 올바른 인덱스 반환
- [x] 배열에 없는 값 검색 -> NotFound 반환

## 삽입 위치 (insertionPoint)
- [x] NotFound 시 삽입 위치 반환 (값이 들어갈 정렬된 위치)
- [x] 모든 요소보다 작은 값 -> insertionPoint = 0
- [x] 모든 요소보다 큰 값 -> insertionPoint = size

## 제네릭 지원
- [ ] Comparable<T>를 구현한 타입에서 동작 (String 테스트)
- [ ] 커스텀 Comparable 객체에서 동작
