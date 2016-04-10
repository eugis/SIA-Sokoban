package sokoban;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import model.Heuristic;
import model.Square;

public class SokobanHeuriticNoAdmisible implements Heuristic {

	public int getHValue(SokobanExpandedState ss) {
		Square[][] board = ss.getBoard();
		Point playerPosition = ss.getPlayerPosition();
		Queue<Point> queue = new LinkedList<Point>();
		int rows = board.length;
		int columns = board[0].length;
		int[][] dist = new int[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				if (i < rows - 1 && j < columns - 1) {
					if (board[i][j].canBeBlocked()
							&& board[i][j + 1].canBeBlocked()
							&& board[i + 1][j].canBeBlocked()
							&& board[i + 1][j + 1].canBeBlocked()) {
						if (board[i][j].isBoxInNotGoal()
								|| board[i][j + 1].isBoxInNotGoal()
								|| board[i + 1][j].isBoxInNotGoal()
								|| board[i + 1][j + 1].isBoxInNotGoal())
							return Integer.MAX_VALUE / 2;
					}
				}

				dist[i][j] = Integer.MAX_VALUE;
				if (board[i][j].isGoal()) {
					queue.add(new Point(i, j));
					dist[i][j] = 0;
				}
			}
		}

		int[] dx = { 0, 1, 0, -1, 0 };
		int[] dy = { 1, 0, -1, 0, 1 };
		while (!queue.isEmpty()) {
			Point curr = queue.remove();
			int x = curr.x;
			int y = curr.y;
			for (int i = 0; i < 4; i++) {
				if (!board[x + dx[i]][y + dy[i]].isWall()) {
					if (dist[x][y] + 1 < dist[x + dx[i]][y + dy[i]]) {
						dist[x + dx[i]][y + dy[i]] = dist[x][y] + 1;
						queue.add(new Point(x + dx[i], y + dy[i]));
					}
				}
			}
		}
		int ans = 0;
		int minDistPlayer = Integer.MAX_VALUE;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (board[i][j].isBox()) {
					minDistPlayer = Math.min(
							minDistPlayer,
							Math.abs(playerPosition.x - i)
									+ Math.abs(playerPosition.y - j));
					for (int k = 0; k < 4; k++) {
						if (board[i + dx[k]][j + dy[k]].isWall()
								&& board[i + dx[k + 1]][j + dy[k + 1]].isWall()
								&& !board[i][j].isGoal())
							return Integer.MAX_VALUE / 2;
					}
					ans += (board[i][j].isGoal()?dist[i][j]:dist[i][j]+100);
				}
			}
		}
		return ans + (minDistPlayer) / 2;
	}

}
