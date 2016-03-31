package sokoban;

import gps.GPSEngine;
import gps.GPSNode;

import java.util.Comparator;
import java.util.PriorityQueue;

import model.Square;

class SokobanEngine extends GPSEngine {

	public SokobanEngine() {
		open = new PriorityQueue<GPSNode>(new Comparator<GPSNode>() {
			@Override
			public int compare(GPSNode o1, GPSNode o2) {
				switch (strategy) {
				case BFS:
					return getCost(o1) - getCost(o2);
				case DFS:
					return getCost(o2) - getCost(o1);
				case IDDFS:
					// IDDFS Condition
					return 0;
				case GREEDY:
					// Greedy Condition
					return 0;
				case ASTAR:
					// AStar Condition
					return 0;
				default:
					return 0;
				}
			}
		});
	}
	
	private int getCost(GPSNode node) {
		int cost = node.getCost();
		SokobanState state = (SokobanState) node.getState();
		for (Square[] squareLine: state.getBoard()) {
			for (Square square: squareLine) {
				if (square.isGoal() && square.isBox()) {
					cost -= 100;
				}
			}
		}
		return cost;
	}

}