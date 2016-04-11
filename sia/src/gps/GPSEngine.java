package gps;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;
import gps.exception.NotAppliableException;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import sokoban.SokobanState;

public abstract class GPSEngine {

	protected Queue<GPSNode> open;
	protected Map<GPSState, Integer> bestCosts = new HashMap<GPSState, Integer>();

	protected GPSProblem problem;

	// Use this variable in open set order.
	protected SearchStrategy strategy;
	private long analyzedCounter;
	private long initTime;
	private long explosionCounter;
	protected long depth;

	public GPSEngine() {
		initTime = System.currentTimeMillis();
		explosionCounter = 0;
		analyzedCounter = 0;
	}
	
	public boolean engine(GPSProblem myProblem, SearchStrategy myStrategy){
		return engine(myProblem, myStrategy, Long.MAX_VALUE);
	}
	
	public boolean engine(GPSProblem myProblem, SearchStrategy myStrategy, long depth) {
		open.clear();
		bestCosts.clear();
		this.depth=depth;
		problem = myProblem;
		strategy = myStrategy;
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0);
		boolean finished = false;
		boolean failed = false;
		
		open.add(rootNode);
		bestCosts.put(rootNode.getState(), 0);
		while (!failed && !finished) {
			if (open.size() <= 0) {
				failed = true;
			} else {
				GPSNode currentNode = open.remove();
				if (problem.isGoal(currentNode.getState())) {
					finished = true;
					System.out.println(currentNode.getSolution());
					System.out.println("Solution deep: " + currentNode.getCost());
					System.out.println("Solution cost: " + currentNode.getCost());
					printStatus();
				} else {
					explosionCounter++;
				/*	if (explosionCounter % 50000 == 0) {
						printStatus();
						System.out.println(currentNode.getState());
						System.out.println("Costo: "+currentNode.getCost());
						System.out.println("Heuristica: "+((SokobanState)currentNode.getState()).getH());
					}*/
					explode(currentNode);
				}
			}
		}
		if (finished) {
			System.out.println("OK! solution found!");
			return true;
		} else if (failed && (strategy!=SearchStrategy.IDDFS)) {
			System.err.println("FAILED! solution not found!");
			return false;
		}
		return false;
	}

	private boolean explode(GPSNode node) {
		if (node.getCost()>=depth) {
			return false;
		}
		if (bestCosts.containsKey(node) && bestCosts.get(node.getState()) <= node.getCost()) {
			return false;
		}
		updateBest(node);
		if (problem.getRules() == null) {
			System.err.println("No rules!");
			return false;
		}
		for (GPSRule rule : problem.getRules()) {
			GPSState newState = null;
			try {
				newState = rule.evalRule(node.getState());
			} catch (NotAppliableException e) {
				// Do nothing
			}
			if (newState != null && isBest(newState, node.getCost() + rule.getCost())) {
				GPSNode newNode = new GPSNode(newState, node.getCost() + rule.getCost());
				newNode.setParent(node);
				open.add(newNode);
			}
		}
		return true;
	}

	private boolean isBest(GPSState state, Integer cost) {
		analyzedCounter++;
		return !bestCosts.containsKey(state) || cost < bestCosts.get(state);
	}

	private void updateBest(GPSNode node) {
		bestCosts.put(node.getState(), node.getCost());
	}

	public void printStatus() {
		System.out.println("Analyzed nodes: " + analyzedCounter);
		System.out.println("Expanded nodes: " + explosionCounter);
		System.out.println("Border nodes: " + open.size());
		System.out.println("Solution time: "+(System.currentTimeMillis()-initTime)+" ms");
	}
}
