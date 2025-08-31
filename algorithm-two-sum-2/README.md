# Two Sum II - Input Array Is Sorted

## Description(KR)
이미 내림차순으로 정렬된 1부터 시작하는 정수 배열이 주어졌을 때 , 두 수 의 합이 특정 숫자가 되는 두 수를 구하세요 . 두 수를 각각 , 라고 하고 , 여기서 는 입니다 .numberstargetnumbers[index1]numbers[index2]1 <= index1 < index2 <= numbers.length

두 숫자의 인덱스를 반환하고 , 를 1씩 더하여 길이가 2인 정수 배열로 만듭니다 .index1index2[index1, index2]

테스트는 정답이 정확히 하나만 있도록 생성됩니다 . 동일한 요소를 두 번 사용할 수 없습니다 .

귀하의 솔루션은 일정한 추가 공간만 사용해야 합니다.



예시 1:

입력: 숫자 = [ 2 , 7 , 11, 15], 목표 = 9
출력: [1, 2]
설명: 2와 7의 합은 9입니다. 따라서 인덱스 1 = 1, 인덱스 2 = 2입니다. [1, 2]를 반환합니다.
예 2:

입력: 숫자 = [ 2 , 3, 4 ], 목표 = 6
출력: [1, 3]
설명: 2와 4의 합은 6입니다. 따라서 인덱스 1 = 1, 인덱스 2 = 3입니다. [1, 3]을 반환합니다.
예시 3:

입력: 숫자 = [ -1 , 0 ], 목표 = -1
출력: [1,2]
설명: -1과 0의 합은 -1입니다. 따라서 인덱스 1 = 1, 인덱스 2 = 2입니다. [1, 2]를 반환합니다.


제약 조건:

2 <= numbers.length <= 3 * 104
-1000 <= numbers[i] <= 1000
numbers감소하지 않는 순서 로 정렬됩니다 .
-1000 <= target <= 1000
테스트는 정확히 하나의 솔루션이 존재하도록 생성됩니다 .

## Description(EN)
Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers such that they add up to a specific target number. Let these two numbers be numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.

Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2] of length 2.

The tests are generated such that there is exactly one solution. You may not use the same element twice.

Your solution must use only constant extra space.



Example 1:

Input: numbers = [2,7,11,15], target = 9
Output: [1,2]
Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2. We return [1, 2].
Example 2:

Input: numbers = [2,3,4], target = 6
Output: [1,3]
Explanation: The sum of 2 and 4 is 6. Therefore index1 = 1, index2 = 3. We return [1, 3].
Example 3:

Input: numbers = [-1,0], target = -1
Output: [1,2]
Explanation: The sum of -1 and 0 is -1. Therefore index1 = 1, index2 = 2. We return [1, 2].


Constraints:

2 <= numbers.length <= 3 * 104
-1000 <= numbers[i] <= 1000
numbers is sorted in non-decreasing order.
-1000 <= target <= 1000
The tests are generated such that there is exactly one solution.