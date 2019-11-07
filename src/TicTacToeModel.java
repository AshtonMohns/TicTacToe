
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * A class representing a game of TicTacToe.
 * A main() method was added later to allow the game to be played.
 * 
 * @author Ashton
 * @version 1.0
 */
public class TicTacToeModel {
	
    public static final int SIZE = 3; //Number of 'X' or 'O' in a straight line reqired for a win.
    private char grid[][]; //Stores the grid with the current board.
    private char turn;
    private TicTacToeEnum gameState = TicTacToeEnum.IN_PROGRESS;
    private int nMarks; //Stores the count of marks made on the board.
    private ArrayList<TicTacToeListener> listeners;
    
    /**
     * Constructor that creates a new TicTacToe game with the requested number
     * of rows, columns and values to win. It also calls reset(initialTurn) to
     * create an empty board and set the 
     * 
     * @param initialTurn contains the first char to be placed, must be 'X' or 'O'.
     */
    public TicTacToeModel(char initialTurn){
        this.grid = new char[TicTacToeModel.SIZE][TicTacToeModel.SIZE];
        this.listeners = new ArrayList<>();
        reset(initialTurn);
    }
    
    /**
     * Sets all values within the grid to ' ', and sets the turn to the
     * requested initialTurn.
     * 
     * @param initialTurn contains the first char to be placed, must be 'X' or 'O'.
     */
    public void reset(char initialTurn){
        if(initialTurn != 'X' && initialTurn != 'O'){
            throw new IllegalArgumentException ("You've entered illegal input values.");
        }
        this.turn = initialTurn;
        this.gameState = TicTacToeEnum.IN_PROGRESS;
        this.nMarks = 0;
        for(int x = 0; x < this.grid[0].length; x++){
            for(int y = 0; y < this.grid.length; y++){
                this.grid[x][y] = (' ');
            }
        }
        notifyTicTacToeListeners(-1, -1);
    }
    
    /**
     * Returns either 'X' or 'O' depending on whose turn it is currently.
     * 
     * @return char 'X' or 'O' to indicate which player's turn it is.
     */
    public char getTurn(){
        return this.turn;
    }
    
    /**
     * Returns the current gameState. The values for this can be IN_PROGRESS, 
     * X_WON, O_WON or DRAW.
     * 
     * @return the current gameState.
     */
    public TicTacToeEnum getGameState(){
        return this.gameState;
    }
    
    /**
     * Changes the gameState to either X_WON or O_WON, depending on the input.
     * 
     * @param player contains the winning player 'X' or 'O'
     * @return the new gameState.
     */
    private TicTacToeEnum charToEnum(char player){
        if(player == 'X'){
            this.gameState = TicTacToeEnum.X_WON;
        }
        if(player == 'O'){
            this.gameState = TicTacToeEnum.O_WON;
        }
        return this.getGameState();
    }
    
    /**
     * Places the current character in the requested grid location.
     * Also checks to determine if the placement of a new character causes
     * either player to win.
     * 
     * @param row an int containing the row where the current character 
     * should be placed.
     * @param column an int containing the column where the current character
     * should be placed.
     * @return the updated gameState (a call to the method findWinner()).
     */
    public TicTacToeEnum takeTurn(int row, int column){
        if (row < 0 || column < 0 || row >= TicTacToeModel.SIZE || column >= TicTacToeModel.SIZE ||
                this.grid[row][column] != ' '){
        	JOptionPane.showMessageDialog(null, "Illegal move.");
        	return this.gameState;
        }
        this.grid[row][column] = this.turn;
        this.nMarks++;
        this.findWinner();
        notifyTicTacToeListeners(row, column);
        if(this.turn == 'X'){
            this.turn = 'O';
        }
        else{
            this.turn = 'X';
        }
        return this.gameState;
    }
    
    /**
     * Update all TicTacToeListeners with the changes made to the board.
     * Reset when x or y is -1, otherwise a new piece was placed at x, y.
     * @param x the x position of the move, -1 to reset.
     * @param y the y position of the move, -1 to reset.
     */
    private void notifyTicTacToeListeners(int x, int y) {
    	TicTacToeEvent e;
    	
    	if(x == -1 || y == -1) e = new TicTacToeEvent(this, -1, -1, ' ', this.gameState);
    	else e = new TicTacToeEvent(this, x, y, this.grid[x][y], this.gameState);
    	
		for(TicTacToeListener t : listeners) t.handleTicTacToeEvent(e);
	}
    
    /**
     * Add a listener to be updated when the game changes.
     * @param listener the object that wants to keep track of the model.
     */
    public void addObserver(TicTacToeListener listener) {
    	listeners.add(listener);
    }

	/**
     * This method goes through four sets of for loops. The first set determines
     * if there are any winners due to the horizontal direction. The second set
     * determines if there are any winners due to the vertical direction. The
     * third set determines if there are any winners due to a diagonal from top
     * left to bottom right, and the fourth set determines if anyone has won due
     * to the diagonal from top right to bottom left.
     * 
     * If the method finds a winner, it will call the charToEnum() method, and
     * the winner will be declared through there.
     * 
     * If the method has not found a winner, but nMarks is 9, then the game is
     * declared a draw.
     * 
     * @return the gameState after the function has run.
     */
    private TicTacToeEnum findWinner(){
        char winner = ' '; //Will store the winner if one is found.
        boolean winningLine = false; //Contains true if a winning line is found.
        
        //This section of code checks each horizontal line for winners.
        for(int column = 0; column < TicTacToeModel.SIZE; column++){
            char first = this.grid[0][column];
            if (first != ' '){
                winningLine = true;
                /* Once the program finds that there is a character filled 
                in, it will check the characters to the right of it to see 
                if they are the same. If they are different, the current row
                is not a winning row.
                */
                for(int row = 1; row < TicTacToeModel.SIZE; row++){   
                    if(first != this.grid[row][column]){
                        winningLine = false;
                    }
                }       
                if (winningLine == true){
                    winner = first;
                }
            }
        }
        
        //This section of code checks every vertical column for winners.
        for(int row = 0; row < TicTacToeModel.SIZE; row++){
            char first = this.grid[row][0];
            if (first != ' '){
                winningLine = true;
                /* Once the program finds that there is a character filled
                in, it will check the characters below it to see if they are
                the same. If they are different, the current column is not a
                winning column.
                */
                for(int column = 1; column < TicTacToeModel.SIZE; column++){   
                    if(first != this.grid[row][column]){
                        winningLine = false;
                    }
                }
                if (winningLine == true){
                    winner = first;
                }
            }
        }
        
        //This section of code checks every diagonal from top left to botton 
        //right for winners.
        //The below for statements will usually only iterate once, 
        //unless either nColumns or nRows is greater than the int numToWin.
        char first = this.grid[0][0];
        if (first != ' '){
            winningLine = true;
            /* Once the program finds that there is a character filled
            in, it will check the characters in the diagonal with it to
            determine if they are the same. If it finds they are different,
            this is not a winning line.
            */
            for(int x = 1; x < TicTacToeModel.SIZE; x++){   
                if(first != this.grid[x][x]){
                    winningLine = false;
                }
            }
            if (winningLine == true){
                winner = first;
            }
        }
        
        //This section of code checks every diagonal from top right to botton 
        //left for winners.
        //The below for statements will usually only iterate once, 
        //unless either nColumns or nRows is greater than the int numToWin.
        first = this.grid[0][TicTacToeModel.SIZE - 1];
        if (first != ' '){
            winningLine = true;
            /* Once the program finds that there is a character filled
            in, it will check the characters in the diagonal with it to
            determine if they are the same. If it finds they are different,
            this is not a winning line.
            */
            for(int x = 1; x < TicTacToeModel.SIZE; x++){   
                if(first != this.grid[x][TicTacToeModel.SIZE - 1 - x]){
                    winningLine = false;
                }
            }
            if (winningLine == true){
                winner = first;
            }
        }
        if(winner != ' '){
            this.charToEnum(winner);
        } 
        else if(nMarks == (TicTacToeModel.SIZE * TicTacToeModel.SIZE)){
            this.gameState = TicTacToeEnum.DRAW;
        }
        return this.gameState;
    }
    
    /**
     * Returns the current grid, and allows the user to get a visual 
     * representation of the current game state.
     * 
     * @return a String containing the current grid.
     */
    public String toString(){
        String currentBoard = new String();
        for(int row = 0; row < TicTacToeModel.SIZE; row++){
            for (int column = 0; column < TicTacToeModel.SIZE; column++){
                currentBoard += (this.grid[row][column] + " | ");
            }
            currentBoard += ("\n");
        }
        return currentBoard;
    }
    
    /**
     * The main method allows the user to play a game of TicTacToe.
     * Repeatedly asks for a row and a column then marks the position
     * and checks the game state. When the gameState is no longer IN_PROGRESS,
     * the game ends.
     * 
     * @param args command-line arguments
     */
    public static void main(String args[]) {
        TicTacToeModel game = new TicTacToeModel('X');
        Scanner scanner = new Scanner(System.in);

        do { 
            System.out.println(game.toString());
            System.out.println(game.getTurn() + 
                ": Where do you want to mark? Enter column row");
            int column = scanner.nextInt();
            int row = scanner.nextInt();
            scanner.nextLine();
            game.takeTurn(row, column);
            
        } while (game.getGameState() == TicTacToeEnum.IN_PROGRESS);
        System.out.println(game.getGameState());
        scanner.close();
    }

}
