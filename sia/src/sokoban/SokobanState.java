package sokoban;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import gps.api.GPSState;
import gps.exception.NotAppliableException;
import model.Direction;

public class SokobanState implements GPSState {

	private List<Point> boxes;
	private Point playerPosition;
	private int heuristicValue;

	public SokobanState(Point playerPosition, List<Point> boxes, int heuristicValue) {
		this.boxes = boxes;
		this.playerPosition = playerPosition;
		this.heuristicValue = heuristicValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boxes == null) ? 0 : boxes.hashCode());
		result = prime * result + ((playerPosition == null) ? 0 : playerPosition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SokobanState other = (SokobanState) obj;
		if (boxes == null) {
			if (other.boxes != null)
				return false;
		} else if (!boxes.equals(other.boxes))
			return false;
		if (playerPosition == null) {
			if (other.playerPosition != null)
				return false;
		} else if (!playerPosition.equals(other.playerPosition))
			return false;
		return true;
	}

	public int getH(){
		return heuristicValue;
	}

	@Override
	public SokobanState clone() {
		return new SokobanState(new Point(playerPosition.x, playerPosition.y), new LinkedList<Point>(boxes), heuristicValue);
	}

	public SokobanState move(Direction dir) throws NotAppliableException{
		SokobanExpandedState ses = new SokobanExpandedState(this);
		ses.move(dir);
		return ses.getSokobanState();
	}

	public Point getPlayerPosition() {
		return playerPosition;
	}
	
	public List<Point> getBoxes(){
		return boxes;
	}

	public void setPlayerPosition(Point playerPosition) {
		this.playerPosition = playerPosition;
	}

	@Override
	public String toString() {
		return new SokobanExpandedState(this).toString();
	}
}
