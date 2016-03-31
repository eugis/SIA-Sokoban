package eightPuzzle;

import gps.GPSEngine;
import gps.GPSNode;

import java.util.Comparator;
import java.util.PriorityQueue;

class PuzzleEngine extends GPSEngine {

	public PuzzleEngine() {
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

}