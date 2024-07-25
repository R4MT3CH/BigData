# LC  3. Longest Substring Without Repeating Characters

# Given a string s, find the length of the longest 
# substring
#  without repeating characters. 

# Example 1:

# Input: s = "abcabcbb"
# Output: 3
# Explanation: The answer is "abc", with the length of 3.

class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:


        # empty set to keep track of repeating characters
        charSet = set()

        # left pointer
        l = 0
        res = 0

        # loop through string from right
        for r in range(len(s)):
            # if character is in the set remove the first one and increment l
            while s[r] in charSet:
                charSet.remove(s[l])
                l += 1
            # but if not add character to the set
            charSet.add(s[r])
            res = max(res, r-l+1)
        
        return res


