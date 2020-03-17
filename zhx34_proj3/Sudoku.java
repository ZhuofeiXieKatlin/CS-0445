// Zhuofei Xie 
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Sudoku {
    public static int recentRow = -1; 
    public static int recentCol = -1;
    public static int[][] originalBoard;
    static boolean isFullSolution(int[][] board) {
        // TODO: Complete this method
        int cols = board.length;
        int rows = board[0].length;
        for (int i=0; i<=cols-1; i++ ){
            for (int j=0; j<= rows-1; j++){
                if(board[i][j]==0) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean reject(int[][] board) {

        // TODO: Complete this method
        int cols = board.length;
        int rows = board[0].length;
        int[][] verticalArray = new int[cols][rows];
        int position = 0;
        int []ThreeTimesThree = new int[cols*rows];
        for(int i=0; i<=cols-1; i++){
            for(int j=0; j<=rows-1; j++){
                verticalArray[i][j] = board[j][i];
            }
        }


        for(int i = 0; i<cols; i++) {
            for (int j = i / 3 * 3; j < i / 3 * 3 + 3; j++) {
                for (int k = (i % 3) * 3; k < (i % 3) * 3 + 3; k++) {
                    add(ThreeTimesThree, board[j][k], position);
                    position++;
                }
            }
        }
        int[][] ThreeTimesArray = convert(ThreeTimesThree);

        for (int i=0; i<=cols-1; i++ ){
            for (int j=0; j<= rows-1; j++){
                if(contain(board[i],board[i][j]) || contain(verticalArray[i],verticalArray[i][j]) || contain(ThreeTimesArray[i], ThreeTimesArray[i][j])){
                    return true;
                }
            }
        }
        return false; 
    }

    static boolean contain(int[] Array, int number){
        int occurence = 0;
        for(int i = 0; i<= Array.length-1; i++){
            if((number == Array[i]) && number>0){
                occurence ++;
            }
        }
        if(occurence >1){
            return true;
        }
        return false;
    }

    static int[] add(int[] array, int number, int position){
        array[position] = number;
        return array;
    }

    static int[][] convert(int[] array){
        int[][] Array = new int[9][9];
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                Array[i][j] = array[i*9+j];
            }
        }
        return Array;
    }

    static int[][] extend(int[][] board) {
        int[][] temp = Arrays.copyOf(board,9);
        for(int i=0; i<9; i++ ){
            for(int j=0;j<9; j++ ){
                if(temp[i][j] == 0){
                    temp[i][j] = 1;
                    recentRow = i; 
                    recentCol = j; 
                    return temp;
                }
            }
        }
        // TODO: Complete this method
        return null;
    }

    static int[][] next(int[][] board) {
        int[][] temp = Arrays.copyOf(board,9);
        // TODO: Complete this method
        // System.out.println("The recent row is "+ recentRow + "the recent column is " + recentCol); 
        if(recentRow!=-1 && recentCol !=-1){
            temp = increaseByOne(temp, recentRow, recentCol); 
            if(temp == null) return null; 
            return temp; 
        }
        return null; 
    }

    static int[][] increaseByOne(int[][] board, int a, int b){
        board[a][b] = board[a][b] +1;
        if(board[a][b]>9){
            board[a][b]=0; 
            return null; 
        }
        return board; 
    }

    static void testIsFullSolution() {
        int[][] fullSolution = new int[][]{
                {1,4,9,8,3,6,7,5,2},
                {5,7,6,2,4,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,3,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,5,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {8,9,7,1,5,2,6,4,3},
        };

        int [][] notfullSolutionOne = new int[][]{
                {1,4,9,0,3,6,7,5,2},
                {5,0,6,2,4,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,0,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,0,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {0,9,7,1,5,2,6,4,3},
        };

        System.err.println("If it is a full solution (without 0 in the board)."); 
        if (isFullSolution(fullSolution)) {
            System.err.println("It is a full solution");
           printBoard(fullSolution);
        }else{
            System.err.println("It is not a full solution"); 
            printBoard(fullSolution); 
        }

        System.err.println("If it is a full solution (without 0 in the board)."); 
        if (isFullSolution(notfullSolutionOne)) {
            System.err.println("It is a full solution");
            printBoard(notfullSolutionOne);
        }else{
            System.err.println("It is not a full solution"); 
            printBoard(fullSolution); 
        }
        // TODO: Complete this method
    }

    static void testReject() {
        int[][] notRejected = new int[][]{
                {1,4,9,8,3,6,7,5,2},
                {5,7,6,2,4,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,3,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,5,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {8,9,7,1,5,2,6,4,3},
        };

        int [][] Rejected = new int[][]{
                {1,4,9,8,3,6,7,3,2},
                {5,7,6,2,5,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,3,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,5,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {8,9,7,1,5,2,6,4,3},
        };
        System.err.println("These should NOT be rejected:");
        if (!reject(notRejected)) {
            printBoard(notRejected);
        }

        System.err.println("These should be rejected:");
        if (reject(Rejected)) {
            printBoard(Rejected);
        }
        // TODO: Complete this method
    }

    static void testExtend() {
        int [][] noneedtoExtend = new int[][]{
                {1,4,9,8,3,6,7,5,2},
                {5,7,6,2,4,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,3,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,5,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {8,9,7,1,5,2,6,4,3},
        };
        System.err.println("These cannot be extended:");
        if(extend(noneedtoExtend) == null){
          printBoard(noneedtoExtend);
        }
        int [][] needtoExtend = new int[][]{
                {1,4,9,0,3,6,7,5,2},
                {5,0,6,2,4,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,0,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,0,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {0,9,7,1,5,2,6,4,3},
        };
        System.err.println("These can be extended:");
        printBoard(needtoExtend);
        System.err.println("After extend the board.");
        printBoard(extend(needtoExtend));
        //TODO: Complete this method
    }

    static void testNext() {
        int [][] noneedtotestNext = new int[][]{
                {1,4,9,8,3,6,7,5,2},
                {5,7,6,2,4,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,3,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,5,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {8,9,7,1,5,2,6,4,3},
        };
        System.err.println("These can NOT be next'd:");
        if(!reject(noneedtotestNext)){
            printBoard(noneedtotestNext);
        }

        int [][] needtotestNext1 = new int[][]{
                {1,4,9,1,3,6,7,5,2},
                {5,0,6,2,4,1,9,3,8},
                {2,3,8,5,7,9,1,6,4},
                {7,2,4,0,6,8,5,9,1},
                {6,8,3,9,1,5,4,2,7},
                {9,0,1,4,2,7,3,8,6},
                {3,6,2,7,9,4,8,1,5},
                {4,1,5,6,8,3,2,7,9},
                {0,9,7,1,5,2,6,4,3},
        };
        System.err.println("These can be next'd:");
        printBoard(needtotestNext1);
        System.err.println("After place the next: ");
        if(reject(needtotestNext1)){
            printBoard(next(needtotestNext1));
        }
        
        //TODO: Complete this method
    }

    static void printBoard(int[][] board) {
        if(board == null) {
            System.out.println("No assignment");
            return;
        }
        for(int i = 0; i < 9; i++) {
            if(i == 3 || i == 6) {
                System.out.println("----+-----+----");
            }
            for(int j = 0; j < 9; j++) {
                if(j == 2 || j == 5) {
                    System.out.print(board[i][j] + " | ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.print("\n");
        }
    }

    static int[][] readBoard(String filename) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
        int[][] board = new int[9][9];
        int val = 0;
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                try {
                    val = Integer.parseInt(Character.toString(lines.get(i).charAt(j)));
                } catch (Exception e) {
                    val = 0;
                }
                board[i][j] = val;
            }
        }
        readoriginalBoard(board);
        return board;
    }

    static void readoriginalBoard(int[][] board){
        originalBoard = Arrays.copyOf(board, 9);
    }
    static int[][] solve(int[][] board) {
        int a = recentRow; 
        int b = recentCol;  
        if(reject(board)) return null;
        if(isFullSolution(board)) return board;
        // System.out.println("The attempt is4!!!!!"); 
        //     printBoard(board); 
        int[][] attempt = extend(board); 
        // System.out.println("The attempt is1!!!!!"); 
        //     printBoard(attempt); 
        while (attempt != null) {
            // System.out.println("The attempt is2!!!!!"); 
            // printBoard(attempt); 
            int[][] solution = solve(attempt);
            if(solution != null) return solution;
               attempt = next(attempt);
            // System.out.println("The attempt is3!!!!!"); 
            // printBoard(attempt); 
        }
        recentRow = a;
        recentCol = b; 
        return null;
    }

    public static void main(String[] args) {
        if(args[0].equals("-t")) {
            testIsFullSolution();
            testReject();
            testExtend();
            testNext();
        } else {
            long startTime = System.nanoTime(); 
            int[][] board = readBoard(args[0]);
            printBoard(board);
            System.out.println("Solution:");
            printBoard(solve(board));
            long endTime = System.nanoTime(); 
            long totalTime = endTime - startTime;
            System.out.println(totalTime/1000000000);  
        }
    }
}

