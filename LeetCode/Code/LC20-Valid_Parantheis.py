#Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
# An input string is valid if:

# Open brackets must be closed by the same type of brackets.
# Open brackets must be closed in the correct order.
# Every close bracket has a corresponding open bracket of the same type.
 
# Example 1:
# Input: s = "()"
# Output: true
# Example 2:

# Input: s = "()[]{}"
# Output: true
# Example 3:

# Input: s = "(]"
# Output: false

class Solution:
    def isValid(self, s: str) -> bool:

        # stack to append and pop the paranthesis chars
        Stack = []
        #close to open paranthesis hashmap
        HashMap = {")":"(","]":"[","}":"{"}

        for c in s:
            if c in HashMap:
                if Stack and Stack[-1] == HashMap[c]:
                    Stack.pop()
                else:
                    return False
            else:
                Stack.append(c)