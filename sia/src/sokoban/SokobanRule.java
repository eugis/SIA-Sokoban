package sokoban;

import gps.api.GPSRule;
import gps.api.GPSState;
import gps.exception.NotAppliableException;
import model.Direction;

public class SokobanRule implements GPSRule {

	Direction dir;

	public SokobanRule(Direction dir) {
		this.dir = dir;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return "Move "+dir.toString();
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		SokobanState ss = (SokobanState) state;
		return ss.move(dir);
	}

}
