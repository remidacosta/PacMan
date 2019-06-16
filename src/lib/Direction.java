package lib;

import java.util.Random;

public enum Direction 
{
	haut(-1,0),
	bas(1,0),
	droite(0,1),
	gauche(0,-1);
	
	private int x;
	private int y;
		
	Direction(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public static Direction DirectionAleatoire() {
	    int pick = new Random().nextInt(Direction.values().length);
	    return Direction.values()[pick];
	}
	
	public Direction getOpposee()
	{
		Direction opposee=null;
		if(this==Direction.bas) opposee=Direction.haut;
		if(this==Direction.haut) opposee=Direction.bas;
		if(this==Direction.gauche) opposee=Direction.droite;
		if(this==Direction.droite) opposee=Direction.gauche;
		return opposee;
	}
	
}
