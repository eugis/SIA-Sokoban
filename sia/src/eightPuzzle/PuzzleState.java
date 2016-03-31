package eightPuzzle;

import java.awt.Point;
import java.util.Arrays;

import gps.api.GPSState;

public class PuzzleState implements GPSState {
	static PuzzleState finalState;

	static int LENGTH = 3;
	static int BLANK = -1;

	int[][] map = new int[LENGTH][LENGTH];

	public PuzzleState(int[][] map) {
		this.map = map;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		int i = 0;
		for (int[] row : map) {
			for (int slot : row) {
				if (slot != -1) {
					hash += Math.pow(10, i) * slot;
				}
				i++;
			}
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PuzzleState other = (PuzzleState) obj;
		if (!Arrays.deepEquals(map, other.map))
			return false;
		return true;
	}

	public int[][] getMap() {
		return map;
	}

	public Point getBlankCoords() {
		int x, y;
		for (x = 0; x < LENGTH; x++) {
			for (y = 0; y < LENGTH; y++) {
				if (map[x][y] == BLANK) {
					return new Point(x, y);
				}
			}
		}
		throw new IllegalStateException();
	}

	@Override
	public String toString() {
		String ret = "---------\n";
		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < LENGTH; j++) {
				ret += String.valueOf(map[i][j]) + ' ';
			}
			ret += '\n';
		}
		return ret;
	}

	static public PuzzleState finalState() {
		if (finalState == null) {
			int[][] map = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, -1 } };
			finalState = new PuzzleState(map);
		}
		return finalState;
	}

}
