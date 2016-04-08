package sokoban;

import model.Square;

public class StaticBoard {

	static private Square[][] board;
	
	static public void initializeStaticBoard(Square[][] board) {
		StaticBoard.board = new Square[board.length][board[0].length];
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				StaticBoard.board[i][j]=board[i][j].clone();
				StaticBoard.board[i][j].setEmpty();
			}
		}
	}
	
	static public Square[][] getBoard(){
		return board;
	}
	
}
