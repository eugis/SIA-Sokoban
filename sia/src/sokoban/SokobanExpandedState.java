package sokoban;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import gps.exception.NotAppliableException;
import model.Direction;
import model.Square;
import utils.Copies;
import utils.Hungarian;

public class SokobanExpandedState {
	
	private Square[][] board;
	private Point playerPosition;
	
	public SokobanExpandedState(SokobanState ss) {
		board = Copies.deepCopy(StaticBoard.getBoard());
		playerPosition = ss.getPlayerPosition();
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
		List<Point> boxes = new LinkedList<Point>();
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j].isBox()){
					boxes.add(new Point(i,j));
				}
			}
		}
		return new SokobanState(playerPosition, boxes, getHValue2());
	}
	
	private int getHValue2(){
		return Hungarian.solveAssignmentProblem(this);
	}
	
	private int getHValue(){
		Queue<Point> queue = new LinkedList<Point>();
		int rows = board.length;
		int columns = board[0].length;
		int[][] dist = new int[rows][columns];
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				
				if(i<rows-1 && j<columns-1){
					if(board[i][j].canBeBlocked() && board[i][j+1].canBeBlocked() && board[i+1][j].canBeBlocked() && board[i+1][j+1].canBeBlocked()){
						if(board[i][j].isBoxInNotGoal() || board[i][j+1].isBoxInNotGoal() || board[i+1][j].isBoxInNotGoal() || board[i+1][j+1].isBoxInNotGoal())
							return Integer.MAX_VALUE/2; 
					}
				}
				
				dist[i][j] = Integer.MAX_VALUE;
				if (board[i][j].isGoal()) {
					queue.add(new Point(i, j));
					dist[i][j] = 0;
				}
			}
		}

		int[] dx = {0,1,0,-1,0};
		int[] dy = {1, 0, -1, 0,1};
		while(!queue.isEmpty()){
			Point curr = queue.remove();
			int x = curr.x;
			int y = curr.y;
			for(int i=0; i<4; i++){
				if(!board[x+dx[i]][y+dy[i]].isWall()){
					if(dist[x][y]+1 < dist[x+dx[i]][y+dy[i]]){
						dist[x+dx[i]][y+dy[i]]=dist[x][y]+1;
						queue.add(new Point(x+dx[i],y+dy[i]));
					}
				}
			}
		}
		int ans = 0;
		int minDistPlayer = Integer.MAX_VALUE;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(board[i][j].isBox()){
					minDistPlayer=Math.min(minDistPlayer, Math.abs(playerPosition.x-i)+Math.abs(playerPosition.y-j));
					for(int k=0; k<4; k++){
						if(board[i+dx[k]][j+dy[k]].isWall() && board[i+dx[k+1]][j+dy[k+1]].isWall() && !board[i][j].isGoal())
							return Integer.MAX_VALUE/2;
					}
					ans+=dist[i][j];
				}
			}
		}
		return ans+(minDistPlayer)/2;
	}
}
