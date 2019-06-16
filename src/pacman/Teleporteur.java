package pacman;

import lib.Case;
import lib.Direction;
import lib.Position;

public class Teleporteur extends Case
{
	private int id;
	private boolean accessible;
	private Direction dirSortie;
	public static int nbCaseTeleportation = 0;
	
	Teleporteur(Position pos, int id)
	{
		super(pos);
		this.accessible = true;
		nbCaseTeleportation++;
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setAccessible(boolean etat)
	{
		accessible = etat;
	}
	
	public boolean getAccessible()
	{
		return accessible;
	}
	
	public void setDirSortie(Direction dir)
	{
		dirSortie = dir;
	}
	
	public Direction getDirSortie()
	{
		return dirSortie;
	}
}
