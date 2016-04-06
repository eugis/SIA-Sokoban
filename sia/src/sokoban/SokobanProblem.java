package sokoban;

import gps.GPSEngine;
import gps.SearchStrategy;
import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import model.Direction;
import model.Square;
import model.SquareType;

public class SokobanProblem implements GPSProblem {

	static GPSEngine engine;
	private GPSState initState;
	
	public SokobanProblem() {
		initializeState();
	}

	public static void main(String[] args) {
		engine = new SokobanEngine();
		SokobanProblem problem = new SokobanProblem();
		SearchStrategy strategy = SearchStrategy.ASTAR; //TODO: Levantar strategy desde properties.
		try {
			if(strategy==SearchStrategy.IDDFS){
				boolean flag=true;
				for(long depth=5; flag; depth+=3){
					flag=!engine.engine(problem, strategy, depth);
					
				}
			}else
				engine.engine(problem, strategy, Long.MAX_VALUE);
			
		} catch (StackOverflowError e) {
			System.out.println("Solution (if any) too deep for stack.");
		} catch (OutOfMemoryError e) {
			System.out.println("Solution (if any) too deep for memory.");
		}
		/*SokobanProblem a = new SokobanProblem();
		System.out.println(a.getHValue(a.getInitState()));*/
	}

	@Override
	public GPSState getInitState() {
		return initState;
	}
	
	private void initializeState() {
		Scanner s = new Scanner(System.in);
		int rows = s.nextInt();
		int columns = s.nextInt();
		s.nextLine();
		Square[][] map = new Square[rows][columns];
		Point playerPosition = null;
		for (int i = 0; i < rows; i++) {
			String nextLine = s.nextLine();
			for (int j = 0; j < columns; j++) {
				char nextChar = Character.toLowerCase(nextLine.charAt(j));
				if (nextChar == 'p') {
					playerPosition = new Point(i, j);
				}
				map[i][j] = getSquare(nextChar);
			}
		}
		s.close();
		if (playerPosition == null) {
			System.err.println("Player position not found.");
		}
		initState = new SokobanState(map, playerPosition);
	}

	private Square getSquare(char squareChar) {
		switch(squareChar) {
		case 'w':
			return new Square(SquareType.Wall, false, false);
		case ' ':
			return new Square(SquareType.Empty, false, false);
		case 'B':
			return new Square(SquareType.Goal, false, true);
		case 'b':
			return new Square(SquareType.Empty, false, true);
		case 'P':
			return new Square(SquareType.Goal, true, false);
		case 'p':
			return new Square(SquareType.Empty, true, false);
		case 'g':
			return new Square(SquareType.Goal, false, false);
		default:
			System.err.println("Error: Unknown character: " + squareChar);
			return null;
		}
	}

	@Override
	public boolean isGoal(GPSState state) {
		for (Square[] squareLine: ((SokobanState) state).getBoard()) {
			for (Square square: squareLine) {
				if (square.isGoal() && !square.isBox()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public List<GPSRule> getRules() {
		List<GPSRule> rules = new LinkedList<GPSRule>();
		for (Direction d : Direction.values()) {
			rules.add(new SokobanRule(d));
		}
		return rules;
	}

	// Valor Heurística para A*
	@Override
	public Integer getHValue(GPSState state) {
		Queue<Point> queue = new LinkedList<Point>();
		SokobanState ss = (SokobanState) state;
		Square[][] board = ss.getBoard();
		
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
		Point playerPoint = ss.getPlayerPosition();
		int minDistPlayer = Integer.MAX_VALUE;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(board[i][j].isBox()){
					minDistPlayer=Math.min(minDistPlayer, Math.abs(playerPoint.x-i)+Math.abs(playerPoint.y-j));
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
