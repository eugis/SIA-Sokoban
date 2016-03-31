package sokoban;

import gps.GPSEngine;
import gps.SearchStrategy;
import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import model.Direction;
import model.Square;
import model.SquareType;

public class SokobanProblem implements GPSProblem {

	static GPSEngine engine;

	public static void main(String[] args) {
		engine = new SokobanEngine();
		try {
			engine.engine(new SokobanProblem(), SearchStrategy.BFS);
		} catch (StackOverflowError e) {
			System.out.println("Solution (if any) too deep for stack.");
		}
	}

	@Override
	public GPSState getInitState() {
		Scanner s = new Scanner(System.in);
		int size = s.nextInt();
		s.nextLine();
		Square[][] map = new Square[size][size];
		Point playerPosition = null;
		for (int i = 0; i < size; i++) {
			String nextLine = s.nextLine();
			for (int j = 0; j < size; j++) {
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
		return new SokobanState(map, playerPosition);
	}

	private Square getSquare(char squareChar) {
		switch(squareChar) {
		case 'w':
			return new Square(SquareType.Wall, false, false);
		case ' ':
			return new Square(SquareType.Empty, false, false);
		case 'b':
			return new Square(SquareType.Empty, false, true);
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
		return 0;
	}

}
