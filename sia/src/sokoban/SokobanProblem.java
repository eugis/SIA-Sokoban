package sokoban;

import gps.GPSEngine;
import gps.SearchStrategy;
import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import model.Direction;
import model.Heuristic;
import model.Square;
import model.SquareType;
import utils.Hungarian;

public class SokobanProblem implements GPSProblem {

	static GPSEngine engine;
	private GPSState initState;
	static private int board;

	public SokobanProblem() {
		initializeState();
	}

	/*
	 * 1er arg = tablero 2do arg = busqueda 3er arg = heuristica
	 * 
	 * busqueda: 1) bfs 2) dfs 3) iddfs 4) greedy 5) asstar
	 */
	public static void main(String[] args) {
		if (args.length < 2 || args.length > 3) {
			System.out
					.println("Argumentos: tablero tipoDeBusqueda [heuristica]");
			System.out
					.println("Tipo de busqueda:\n1)\tbfs\n2)\tdfs\n3)\tiddfs\n4)\tgreedy\n5)\tasstar\n");
			return;
		}
		board = Integer.valueOf(args[0]);
		int search = Integer.valueOf(args[1]);
		int heuristic=0;
		try {
			board = Integer.valueOf(args[0]);
			search = Integer.valueOf(args[1]);
			if (search == 4 || search == 5) {
				heuristic = Integer.valueOf(args[2]);
				if(heuristic<1 || heuristic>2)
					throw new Exception();
			}
			if(board<1 || search<1 || search>5)
				throw new Exception();
		} catch (Exception e) {
			System.out.println("Argumentos invalidos");
			return;
		}
		SearchStrategy strategy = SearchStrategy.values()[search-1];
		if(heuristic==1)
			SokobanExpandedState.h=new SokobanHeuristic();
		else
			SokobanExpandedState.h=new Heuristic() {
				@Override
				public int getHValue(SokobanExpandedState ss) {
					return Hungarian.solveAssignmentProblem(ss);
				}
			};
		engine = new SokobanEngine();
		SokobanProblem problem = new SokobanProblem();
		
		try {
			if (strategy == SearchStrategy.IDDFS) {
				boolean flag = true;
				for (long depth = 5; flag; depth += 3) {
					flag = !engine.engine(problem, strategy, depth);

				}
			} else
				engine.engine(problem, strategy, Long.MAX_VALUE);

		} catch (StackOverflowError e) {
			System.out.println("Solution (if any) too deep for stack.");
		} catch (OutOfMemoryError e) {
			System.out.println("Solution (if any) too deep for memory.");
		}
		/*
		 * SokobanProblem a = new SokobanProblem();
		 * System.out.println(a.getHValue(a.getInitState()));
		 */
	}

	@Override
	public GPSState getInitState() {
		return initState;
	}

	private void initializeState() {
		Scanner s = null;
		try {
			s = new Scanner(new File("boards/board-"+board));
		} catch (FileNotFoundException e) {
			System.err.println("Archivo no encotrado");
			return;
		}
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
		initState = new SokobanExpandedState(playerPosition, map)
				.getSokobanState();
	}

	private Square getSquare(char squareChar) {
		switch (squareChar) {
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
		for (Point box : ss.getBoxes()) {
			if (!board[box.x][box.y].isGoal())
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
		return ((SokobanState) state).getH();
	}

}
