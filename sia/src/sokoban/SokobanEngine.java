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
					return o1.getCost() - o2.getCost();
				case DFS:
					return o2.getCost() - o1.getCost();
				case IDDFS:
					// IDDFS Condition
					return 0;
				case GREEDY:
					return (problem.getHValue(o1.getState()))
							- (problem.getHValue(o2.getState()));
				case ASTAR:
					return (problem.getHValue(o1.getState()) + o1.getCost())
							- (problem.getHValue(o2.getState()) + o2.getCost());
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