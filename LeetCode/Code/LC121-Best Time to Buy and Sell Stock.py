class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        left,right = 0,1 #left=buy, right=sell

        maxProfit = 0

        while right<len(prices):
            #profitable?
            if prices[left]<prices[right]:
                profit = prices[right] - prices[left]
                maxProfit = max(maxProfit, profit)
            else:
                left = right
            
            right += 1
        return maxProfit