import java.util.*;

/*
 * Author: Gregory Dott
 * 03-11-2022
 * 
 * From Wikipedia:
 * =========================================================================================================================
 * In computer science, counting sort is an algorithm for sorting a collection of objects according to keys 
 * that are small positive integers; that is, it is an integer sorting algorithm. It operates by counting the 
 * number of objects that possess distinct key values, and applying prefix sum on those counts to determine the
 * positions of each key value in the output sequence. Its running time is linear in the number of items and the 
 * difference between the maximum key value and the minimum key value, so it is only suitable for direct 
 * use in situations where the variation in keys is not significantly greater than the number of items. It is 
 * often used as a subroutine in radix sort, another sorting algorithm, which can handle larger keys more efficiently. 
 * =========================================================================================================================
 * 
 * See http://www.cs.usfca.edu/~galles/visualization/CountingSort.html for a visualisation of the algorithm in action.
 * 
 * Below is a very simple implementation of counting sort. It can easily be adapted for use with more advanced objects.
 *
 * TODO: implement this with a list of objects instead of just an array.
 * 
 * Although it technically is not possible to sort a list containing negative numbers with this algorithm 
 * in its current form, there is at least one way around this:
 * Find the value of the lowest negative number, and add the absolute value of that plus one to every number.
 * Also add that to the previous k value.
 * Now all numbers will be positive. Once sorted, simply subtract (abs(lowest negative) + 1) from every number
 * in the sorted array.
 * 
 */

public class CountingSort {
    
    public static void main(String args[]) {
        int[] nums = {4, 2, 10, 10, 1, 4, 2, 1, 10}; // nums[n] always non-negative (this sort relies on creating an array with indices relating to values to be sorted)
        int k = 10; // max value in nums

        Pr.x("Unsorted: " + Arrays.toString(nums));
        int[] sorted = countingSort(nums, k);

        Pr.x("Sorted: " + Arrays.toString(sorted));
    }

    private static int[] countingSort(int[] nums, int k) {
        int[] count = new int[k+1]; // records the number of times each element appears in the sequence (we use k+1 so that we have index from 0 to k)
        int[] sorted = new int[nums.length];

        // Here we count and store the number of occurences of each value in the count array. This is used to calculate the prefix sums in the next loop
        for (int i = 0; i < nums.length; i++) {
            //j = key(nums[i]) -- this will be what we use when we use objects. just using an array now, so j = nums[i] as these are our keys.
            int j = nums[i];
            count[j] = count[j] + 1;
        }

        // Now replace count values with calculated prefix sums. The prefix sums are crucial to finding the ordering of each element.
        // The prefix sum in count for a number that is in nums represents the number of elements with lower value than the current number 
        // plus the number of elements of the current value. So, it follows that a prefix sum calculated at this point will be the position
        // in a 1 based array of the last element of that value. This gets used in the next loop to find where each element belongs.
        for (int i = 1; i <= k; i++) {
            count[i] = count[i] + count[i-1];
        }

        // If we want to preserve the relative order of equal value items in the sorted array, we must loop down from the end of the array.
        // We can loop in the opposite direction, from 0 upwards as well, but then the relative order of equal value items will be reversed.
        //-------------------
        // Here our prefix sums are used to find where each element belongs. For each element of a certain value, we get the prefix sum for 
        // that value, decrement it, and put the element there. The next element of this value, naturally then goes to the left of this one
        // when we come across it in the array.
        for (int i = nums.length - 1; i >= 0; i--) {
            //j = key(input[i]) -- for object implementation
            int j = nums[i];
            count[j] = count[j] - 1;
            sorted[count[j]] = nums[i];
        }

        return sorted;
    }
}
