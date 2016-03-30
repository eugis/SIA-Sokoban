package model;

public class Square {
	
	private boolean player, box;
	
	private SquareType type;
	
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
	
}
