package sokoban;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import gps.GPSEngine;
import gps.SearchStrategy;
import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;
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
		SearchStrategy strategy = SearchStrategy.GREEDY; //TODO: Levantar strategy desde properties.
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
		StaticBoard.initializeStaticBoard(map);
		initState = new SokobanExpandedState(playerPosition,map).getSokobanState();
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
		Square[][] board = StaticBoard.getBoard();
		SokobanState ss = (SokobanState) state;
		for(Point box : ss.getBoxes()){
			if(!board[box.x][box.y].isGoal())
				return false;
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
		return ((SokobanState)state).getH();
	}

}
