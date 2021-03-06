package utils;

import model.Square;

public class Copies {

	public static Square[][] deepCopy(Square[][] param) {
		Square[][] ret = new Square[param.length][];
		for (int i = 0; i < param.length; i++) {
			ret[i] = new Square[param[i].length];
			for (int j = 0; j < param[0].length; j++) {
				ret[i][j] = param[i][j].clone();
			}
		}
		return ret;
	}

	public static int[][] deepCopy(int[][] param) {
		int[][] ret = new int[param.length][];
		for (int i = 0; i < param.length; i++) {
			ret[i] = new int[param[i].length];
			for (int j = 0; j < param.length; j++) {
				ret[i][j] = param[i][j];
			}
		}
		return ret;
	}
}
