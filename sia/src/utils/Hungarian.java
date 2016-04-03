package utils;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import model.Square;
import sokoban.SokobanState;

public class Hungarian {
	

	public static int solveAssignmentProblem(SokobanState ss){
		
		Square[][] board = ss.getBoard();
		int rows = board.length;
		int columns = board[0].length;
		int[] dx = {0,1,0,-1};
		int[] dy = {1, 0, -1, 0};
		int box=0;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if (board[i][j].isBox()) box++;
			}
		}
		int[][] a = new int[box+1][box+1];
		int boxCounter=-1;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				
				if (board[i][j].isBox()) {
					int goalCounter=-1;
					boxCounter++;
					int[][] dist = new int[rows][columns];
					for(int k=0; k<rows; k++){
						for(int l=0; l<columns; l++){
							dist[k][l] = Integer.MAX_VALUE;
						}
					}
					dist[i][j] = 0;
					Queue<Point> queue = new LinkedList<Point>();
					queue.add(new Point(i, j));
					while(!queue.isEmpty()){

						Point curr = queue.remove();
						int x = curr.x;
						int y = curr.y;
						for(int ii=0; ii<4; ii++){
							if(!board[x+dx[ii]][y+dy[ii]].isWall()){
								if(dist[x][y]+1 < dist[x+dx[ii]][y+dy[ii]]){
									dist[x+dx[ii]][y+dy[ii]]=dist[x][y]+1;
									queue.add(new Point(x+dx[ii],y+dy[ii]));
								}
							}
						}
					}
					for(int ii=0; ii<rows; ii++){
						for(int jj=0; jj<columns; jj++){
							if(board[ii][jj].isGoal()){
								goalCounter++;
								a[boxCounter+1][goalCounter+1]=dist[ii][jj];
							}
						}
					}
					
				}
			}
		}
		/*for(int i=1; i<box+1; i++){
			for(int j=1; j<box+1; j++){
				System.out.print(a[i][j]+" ");
			}
			System.out.println();
		}*/
		return solveAssignmentProblem(a);
	}
	
	public static int solveAssignmentProblem(int[][] a) {
	    int n = a.length - 1;
	    int m = a[0].length - 1;
	    int[] u = new int[n + 1];
	    int[] v = new int[m + 1];
	    int[] p = new int[m + 1];
	    int[] way = new int[m + 1];
	    for (int i = 1; i <= n; ++i) {
	      p[0] = i;
	      int j0 = 0;
	      int[] minv = new int[m + 1];
	      Arrays.fill(minv, Integer.MAX_VALUE);
	      boolean[] used = new boolean[m + 1];
	      do {
	        used[j0] = true;
	        int i0 = p[j0];
	        int delta = Integer.MAX_VALUE;
	        int j1 = 0;
	        for (int j = 1; j <= m; ++j)
	          if (!used[j]) {
	            int cur = a[i0][j] - u[i0] - v[j];
	            if (cur < minv[j]) {
	              minv[j] = cur;
	              way[j] = j0;
	            }
	            if (minv[j] < delta) {
	              delta = minv[j];
	              j1 = j;
	            }
	          }
	        for (int j = 0; j <= m; ++j)
	          if (used[j]) {
	            u[p[j]] += delta;
	            v[j] -= delta;
	          } else
	            minv[j] -= delta;
	        j0 = j1;
	      } while (p[j0] != 0);
	      do {
	        int j1 = way[j0];
	        p[j0] = p[j1];
	        j0 = j1;
	      } while (j0 != 0);
	    }
	    return -v[0];
	  }
}
