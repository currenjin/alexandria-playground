# Two Sum 

## Description(KR)

정수 배열 nums 과 정수가 주어지면 두 숫자의 인덱스를 target반환하여 두 숫자의 합이 .가 되도록 합니다target .

각 입력에 대해 정확히 하나의 솔루션이 있다고 가정할 수 있으며 , 동일한 요소를 두 번 사용할 수 없습니다 .

답변은 원하는 순서대로 작성하시면 됩니다.



예시 1:

입력: nums = [2,7,11,15], target = 9
출력: [0,1]
설명: nums[0] + nums[1] == 9이므로 [0, 1]을 반환합니다.
예 2:

입력: nums = [3,2,4], target = 6
출력: [1,2]
예시 3:

입력: nums = [3,3], target = 6
출력: [0,1]


제약 조건:

2 <= nums.length <= 104
-109 <= nums[i] <= 109
-109 <= target <= 109
유효한 답변은 단 하나뿐입니다.


후속 질문:  시간 복잡도 보다 낮은 알고리즘을 생각해 낼 수 있나요 ?O(n2)

## Description(EN)

Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.



Example 1:

Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
Example 2:

Input: nums = [3,2,4], target = 6
Output: [1,2]
Example 3:

Input: nums = [3,3], target = 6
Output: [0,1]


Constraints:

2 <= nums.length <= 104
-109 <= nums[i] <= 109
-109 <= target <= 109
Only one valid answer exists.


Follow-up: Can you come up with an algorithm that is less than O(n2) time complexity?