# Given an integer x, return true if x is a 
# palindrome
# , and false otherwise.

class Solution:
    def isPalindrome(self, x: int) -> bool:

        # if number is negative its False
        if x<0:
            return False
        
        # count number of digits in the number
        div = 1
        while x>=10 * div:
            div *=10
        
        # Extract left and right digit and compare them together
        while x: 
            right = x % 10
            left = x //div

            # if not equal its not Palindrome
            if left != right: return False

            # Get Rid of left and right digit
            x = (x % div) // 10
            div = div / 100

        return True