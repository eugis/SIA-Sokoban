package sokoban;

import java.awt.Point;
import java.util.Arrays;

import gps.api.GPSState;
import gps.exception.NotAppliableException;
import model.Direction;
import model.Square;
import utils.Copies;

public class SokobanState implements GPSState {
	
	private Square[][] board;
	private Point playerPosition;
	
	public SokobanState(Square[][] board, Point playerPosition) {
		this.board = board;
		this.playerPosition = playerPosition;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(board);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SokobanState other = (SokobanState) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}

	public Square[][] getBoard() {
		return board;
	}
	
	@Override
	public SokobanState clone(){
		Square[][] newBoard = Copies.deepCopy(this.board); 
		
		SokobanState ss = new SokobanState(newBoard, new Point(playerPosition.x, playerPosition.y));
		return ss;
	}
	
	public void move(Direction dir) throws NotAppliableException{
		int x = playerPosition.x;
		int y = playerPosition.y;
		playerPosition.translate(dir.getDelta().x, dir.getDelta().y);
		int dx = playerPosition.x;
		int dy = playerPosition.y;
		Square sq = board[dx][dy]; 
		if(sq.isBox()){
			int ddx = x+dir.getDelta().x*2;
			int ddy = y+dir.getDelta().y*2;
			Square dsq = board[ddx][ddy];
			if(dsq.isEmpty()){
				dsq.setBox();
			}else{
				throw new NotAppliableException();
			}
		}else if(!sq.isEmpty()){
			throw new NotAppliableException();
		}
		board[x][y].setEmpty();
		board[dx][dy].setPlayer();
	}

	public void setBoard(Square[][] board) {
		this.board = board;
	}

	public Point getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Point playerPosition) {
		this.playerPosition = playerPosition;
	}	
	
	@Override
	public String toString() {
		StringBuilder boardString = new StringBuilder();
		boardString.append('\n');
		for (Square[] squareLine: board) {
			for (Square square: squareLine) {
				boardString.append(square.toString());
			}
			boardString.append('\n');
		}
		return boardString.toString();
	}
}
