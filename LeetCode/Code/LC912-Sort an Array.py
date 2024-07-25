# LC 912. Sort an Array

# Given an array of integers nums, sort the array in ascending order and return it.

# You must solve the problem without using any built-in 
# functions in O(nlog(n)) time complexity and with the smallest space complexity possible.


class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:
        
        def merge(arr, L,M,R):
            left, right = arr[L:M+1], arr[M+1:R+1]
            i, j, k = L, 0, 0

            while j<len(left) and k < len(right):
                if left[j]<= right[k]:
                    arr[i] = left[j]
                    j+=1
                else:
                    arr[i] = right[k]
                    k +=1
                i+= 1
            while j < len(left):
                nums[i]= left[j]
                j += 1
                i += 1
            while k < len(right):
                nums[i] =right[k]
                k+=1
                i+=1

        def mergeSort(arr, l, r):
            if l == r:
                return arr
            m = (l+r) // 2
            mergeSort(arr,l,m)
            mergeSort(arr,m+1,r)
            merge(arr, l, m,r)
            return arr