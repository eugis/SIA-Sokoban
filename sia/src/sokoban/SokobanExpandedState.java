package sokoban;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import gps.exception.NotAppliableException;
import model.Direction;
import model.Heuristic;
import model.Square;
import utils.Copies;

public class SokobanExpandedState {
	
	private Square[][] board;
	private Point playerPosition;
	static public Heuristic h;
	
	public SokobanExpandedState(SokobanState ss) {
		board = Copies.deepCopy(StaticBoard.getBoard());
		playerPosition = new Point(ss.getPlayerPosition());
		board[playerPosition.x][playerPosition.y].setPlayer();
		for(Point p : ss.getBoxes()){
			board[p.x][p.y].setBox();
		}
	}
	
	public SokobanExpandedState(Point playerPosition, Square[][] board){
		this.board=Copies.deepCopy(board);
		this.playerPosition=new Point(playerPosition);
	}
	
	public Square[][] getBoard() {
		return board;
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
	
	public SokobanState getSokobanState() {
		Set<Point> boxes = new HashSet<Point>();
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j].isBox()){
					boxes.add(new Point(i,j));
				}
			}
		}
		return new SokobanState(playerPosition, boxes, h.getHValue(this));
	}
	
	
	
}
