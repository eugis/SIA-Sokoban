package utils;

import model.Square;

public class Copies {

	public static Square[][] deepCopy(Square[][] param) {
		Square[][] ret = new Square[param.length][];
		for (int i = 0; i < param.length; i++) {
			ret[i] = new Square[param[i].length];
			for (int j = 0; j < param.length; j++) {
				ret[i][j] = param[i][j];
			}
		}
		return ret;
	}
}
