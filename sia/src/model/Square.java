package model;

public class Square {
	
	private boolean player, box;
	
	private SquareType type;
	
	public Square(SquareType squareType, boolean player, boolean box) {
		this.type = squareType;
		this.player = player;
		this.box = box;
	}
	
	public boolean isEmpty(){
		return type!=SquareType.Wall && !player && !box;
	}
	
	public boolean isBox(){
		return box;
	}
	
	public boolean isPlayer(){
		return player;
	}
	
	public boolean isWall(){
		return type==SquareType.Wall;
	}
	
	public boolean isGoal(){
		return type==SquareType.Goal;
	}
	
	public void setPlayer() {
		player = true;
		box=false;
	}
	
	public void setBox() {
		box = true;
		player = false;
	}
	
	public void setEmpty(){
		box = false;
		player = false;
	}
	
	public String toString(){
		switch(type){
			case Wall:
				return "w";
			case Empty:
				if(player) return "p";
				if(box) return "b";
				return " ";
			case Goal:
				if(player) return "P";
				if(box) return "B";
				return "G";
		}
		throw new RuntimeException("Cannot convert Square to String");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (box ? 1231 : 1237);
		result = prime * result + (player ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		if (box != other.box)
			return false;
		if (player != other.player)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	public Square clone() {
		return new Square(type, player, box);
	}
	
}
