package eightPuzzle;

import gps.api.GPSRule;
import gps.api.GPSState;
import gps.exception.NotAppliableException;

import java.awt.Point;

import utils.Copies;

public class PuzzleRule implements GPSRule {
	Direction direction;

	public PuzzleRule(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return "Move blank space " + direction.toString();
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		PuzzleState puzzleState = ((PuzzleState) state);
		Point delta = direction.getDelta();
		Point blank = puzzleState.getBlankCoords();
		Point destination = (Point) blank.clone();
		destination.translate(delta.x, delta.y);
		if (destination.getX() < 0 || destination.getX() >= PuzzleState.LENGTH || destination.getY() < 0 || destination.getY() >= PuzzleState.LENGTH) {
			throw new NotAppliableException();
		}
		int[][] newMap = Copies.deepCopy(puzzleState.map);
		newMap[blank.x][blank.y] = newMap[destination.x][destination.y];
		newMap[destination.x][destination.y] = PuzzleState.BLANK;
		return new PuzzleState(newMap);
	}
}
