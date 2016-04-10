package model;

import sokoban.SokobanExpandedState;

public interface Heuristic {

	public int getHValue(SokobanExpandedState ss);
}
