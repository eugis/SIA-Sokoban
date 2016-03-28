package eightPuzzle;

import java.awt.Point;

public enum Direction {
	LEFT, DOWN, RIGHT, UP;

	@Override
	public String toString() {
		switch (this) {
		case LEFT:
			return "Left";
		case DOWN:
			return "Down";
		case RIGHT:
			return "Right";
		case UP:
			return "Up";
		default:
			throw new IllegalStateException();
		}
	}

	public Point getDelta() {
		switch (this) {
		case LEFT:
			return new Point(0, -1);
		case DOWN:
			return new Point(1, 0);
		case RIGHT:
			return new Point(0, 1);
		case UP:
			return new Point(-1, 0);
		default:
			throw new IllegalStateException();
		}
	}

}