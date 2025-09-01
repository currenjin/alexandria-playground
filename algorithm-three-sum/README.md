# Three Sum 

## Description(KR)

정수 배열 nums가 주어졌을 때 , , , , , 를 [nums[i], nums[j], nums[k]]만족하는 모든 삼중항을 반환합니다 .i != ji != kj != knums[i] + nums[j] + nums[k] == 0

솔루션 세트에는 중복된 3중항이 포함되어서는 안 됩니다.



예시 1:

입력: nums = [-1,0,1,2,-1,-4]
출력: [[-1,-1,2],[-1,0,1]]
설명:
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
고유한 삼중항은 [-1,0,1]과 [-1,-1,2]입니다.
출력 순서와 삼중항의 순서는 중요하지 않습니다.
예 2:

입력: nums = [0,1,1]
출력: []
설명: 가능한 세 개의 숫자 중 합이 0이 아닌 숫자만 가능합니다.
예시 3:

입력: nums = [0,0,0]
출력: [[0,0,0]]
설명: 가능한 삼중항의 합은 0입니다.


제약 조건:

3 <= nums.length <= 3000
-105 <= nums[i] <= 105

## Description(EN)

Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.

Notice that the solution set must not contain duplicate triplets.



Example 1:

Input: nums = [-1,0,1,2,-1,-4]
Output: [[-1,-1,2],[-1,0,1]]
Explanation:
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
The distinct triplets are [-1,0,1] and [-1,-1,2].
Notice that the order of the output and the order of the triplets does not matter.
Example 2:

Input: nums = [0,1,1]
Output: []
Explanation: The only possible triplet does not sum up to 0.
Example 3:

Input: nums = [0,0,0]
Output: [[0,0,0]]
Explanation: The only possible triplet sums up to 0.


Constraints:

3 <= nums.length <= 3000
-105 <= nums[i] <= 105