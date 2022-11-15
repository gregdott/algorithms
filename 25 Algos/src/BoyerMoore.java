/*
 * Author: Gregory Dott
 * 05-11-2022
 * 
 * From Wikipedia:
 * ================================================================================================================================================
 * The Boyer–Moore majority vote algorithm is an algorithm for finding the majority of a sequence of elements using linear time and constant space. 
 * It is named after Robert S. Boyer and J Strother Moore, who published it in 1981, and is a prototypical example of a streaming algorithm.
 * ================================================================================================================================================
 * 
 * If there is no majority element, a value of -1 is returned
 */

public class BoyerMoore {
    
    public static void main(String args[]) {
        int[] nums = {1, 8, 7, 4, 1, 2, 2, 2, 2, 2, 2,};
        int m = findMajorityElement(nums);
        Pr.x("Majority element: " + m);
    }

    private static int findMajorityElement(int[] nums) {
        int i, m;
        m = i = 0;

        /*
         * My attempt at explaining this algorithm:
         * 
         * We have a sequence element (m) and a counter (i).
         * i starts at 0. m gets initialised to 0 here, but only for the sake of preventing a Java exception.
         * At the beginning of execution m is essentially uninitialised.
         * 
         * We loop through the elements of the given array
         * Our sequence element m, gets set to the current array element when i is 0;
         * If our sequence element is equal to the current array element (on subsequent iterations), we increment i.
         * Otherwise we decrement i. So when we set m = some element, and the subsequent element is not equal to m,
         * we still keep m equal to what it was set to in the previous iteration. Only in the iteration following the current one
         * will the sequence element change if the array element is not equal to it.
         * 
         * This way, if there is an element that appears in the array more times than half the size of the array, m will
         * be equal to that element. If there is no majority element, the value of m is somewhat arbitrary, and that is 
         * why we need to loop through the array once again and confirm that the number of occurrences of the element is
         * indeed greater than half the size of the array.
         * 
         * The way this makes sense in my mind is to picture an arbitrary array with majority element q in various arrangements:
         * [q, e, q, r, q, s, q, l, q, f, q] - here it is easy to see that m will always equal q because it appears at every second index
         * [q, q, q, q, q, q, e, r, s, l, f] - here, we know after reaching index 5, i=5, so by the time we reach the end of the array, 
         * the sequence element will still be q because i will not have reached 0.
         * 
         */
        for (int num: nums) {
            if (i == 0) {
                m = num;
            } else if (m == num) {
                i++;
            } else {
                i--;
            }
        }

        //------------------------------------------------------------------------------------------------------------------------------
        // confirm majority element (if there is no majority element, m = some arbitrary element, so need to confirm)
        int count = 0;
        for (int num: nums) {
            if (num == m) {
                count++;
            }
        }

        // number of occurences is less than half the array size, so there is no majority element
        if (count < nums.length/2) {
            m = -1;
        }
        //------------------------------------------------------------------------------------------------------------------------------

        return m;
    }
}
