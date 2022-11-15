import java.util.*;

/*
 * Author: Gregory Dott
 * 01-11-2022
 * 
 * From Wikipedia:
 * ==============================================================================================================================================
 * Flood fill, also called seed fill, is a flooding algorithm that determines and alters the area connected to a given node in a 
 * multi-dimensional array with some matching attribute. It is used in the "bucket" fill tool of paint programs to fill connected, 
 * similarly-colored areas with a different color, and in games such as Go and Minesweeper for determining which pieces are cleared. 
 * A variant called boundary fill uses the same algorithms but is defined as the area connected to a given node that does not have a particular attribute.
 * ==============================================================================================================================================
 * 
 * 2 implementations are provided:
 * 
 * 1.) The four-way, recursive version
 * 2.) Data-structure based version
 * 
 */

public class FloodFill {
    public static void main(String args[]) {
        char[][] matrix1 = {
            {'Y', 'Y', 'Y', 'G', 'G', 'G', 'G', 'G', 'G', 'G', },
            {'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'G', 'X', 'X', 'X', },
            {'G', 'G', 'G', 'G', 'G', 'G', 'G', 'X', 'X', 'X', },
            {'W', 'W', 'W', 'W', 'W', 'G', 'G', 'G', 'G', 'X', },
            {'W', 'R', 'R', 'R', 'R', 'R', 'G', 'X', 'X', 'X', },
            {'W', 'W', 'W', 'R', 'R', 'G', 'G', 'X', 'X', 'X', },
            {'W', 'B', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'X', },
            {'W', 'B', 'B', 'B', 'B', 'R', 'R', 'X', 'X', 'X', },
            {'W', 'B', 'B', 'X', 'B', 'B', 'B', 'B', 'X', 'X', },
            {'W', 'B', 'B', 'X', 'X', 'X', 'X', 'X', 'X', 'X', }};
        

        Pr.x("=========================================================================");
        Pr.x("Original Matrix:");
        printMatrix(matrix1);
        Pr.x("=========================================================================");

        char[][] matrix2 = new char[matrix1.length][matrix1.length];
        // clone matrix1 completely (.clone() is shallow so we must do it for each subarray)
        for (int i = 0; i < matrix1.length; i++) matrix2[i] = matrix1[i].clone();

        Pr.x("=========================================================================");
        Pr.x("Flood fill recursive: ");
        floodFillRecursive(matrix1, new int[]{3, 9}, matrix1[3][9], 'C');
        printMatrix(matrix1);
        Pr.x("=========================================================================");
        
        Pr.x("=========================================================================");
        Pr.x("Flood fill iterative: ");
        floodFillIterative(matrix2, new int[]{3, 9}, matrix2[3][9], 'C');
        printMatrix(matrix2);
        Pr.x("=========================================================================");
        
    }

    /**
     * floodFillRecursive - recursive implementation of the Flood Fill algorithm
     * 
     * @param matrix nxn matrix of chars representing colour values of pixels in a grid
     * @param node int[2] representing the starting point of the fill (node[0] -> x, node[1] -> y)
     * @param initialColour the colour at the point that we are to begin replacing from
     * @param newColour the new colour we are replacing the old colour with
     */
    private static void floodFillRecursive(char[][] matrix, int[] node, char initialColour, char newColour) {
        if (matrix[node[0]][node[1]] != initialColour) { // don't want to replace colours that are not our initial colour
            return;
        } else {
            matrix[node[0]][node[1]] = newColour;
        }

        int length = matrix.length;
        List<int[]> neighbours = new ArrayList<int[]>(); 
        // The 4 lines below basically look to the left, the right, below and above the current position
        if (node[0] - 1 >= 0) neighbours.add(new int[]{node[0] - 1, node[1]});
        if (node[0] + 1 < length) neighbours.add(new int[]{node[0] + 1, node[1]});
        if (node[1] - 1 >= 0) neighbours.add(new int[]{node[0], node[1] - 1});
        if (node[1] + 1 < length) neighbours.add(new int[]{node[0], node[1] + 1});

        for(int[] neighbour: neighbours) {
            floodFillRecursive(matrix, neighbour, initialColour, newColour);
        }
    }

    /**
     * floodFillIterative - iterative implementation of the Flood Fill algorithm
     * 
     * @param matrix nxn matrix of chars representing colour values of pixels in a grid
     * @param node int[2] representing the starting point of the fill (node[0] -> x, node[1] -> y)
     * @param initialColour the colour at the point that we are to begin replacing from
     * @param newColour the new colour we are replacing the old colour with
     */
    private static void floodFillIterative(char[][] matrix, int[] startNode, char initialColour, char newColour) {
        int length = matrix.length;
        List<int[]> toFill = new ArrayList<int[]>();
        toFill.add(startNode);

        while(!toFill.isEmpty()) {
            int[] node = toFill.get(0);
            matrix[node[0]][node[1]] = newColour;
            toFill.remove(node);
            // The 4 lines below basically look to the left, the right, below and above the current position
            if (node[0] - 1 >= 0 && matrix[node[0] - 1][node[1]] == initialColour) toFill.add(new int[]{node[0] - 1, node[1]});
            if (node[0] + 1 < length && matrix[node[0] + 1][node[1]] == initialColour) toFill.add(new int[]{node[0] + 1, node[1]});
            if (node[1] - 1 >= 0 && matrix[node[0]][node[1] - 1] == initialColour) toFill.add(new int[]{node[0], node[1] - 1});
            if (node[1] + 1 < length && matrix[node[0]][node[1] + 1] == initialColour) toFill.add(new int[]{node[0], node[1] + 1});
        }
    }

    /**
     * printMatrix - does what it says on the tin
     * @param matrix n x n matrix of ints
     */
    private static void printMatrix(char[][] matrix) {
        for (char[] row: matrix) {
            Pr.x(Arrays.toString(row));
        }
    }    
}