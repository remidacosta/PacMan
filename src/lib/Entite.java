package lib;

import java.util.LinkedList;
import java.util.Observable;

import pacman.TerrainPacman;

public abstract class Entite extends Observable implements Runnable
{
	protected LinkedList<Position> FilePos;
	protected Direction direction;
	protected int vitesseDeplacement = 400;

	public abstract void Deplacer();
	
	public boolean estPositionPerso(int x, int y)
	{
		return (x==FilePos.getFirst().getX() && y==FilePos.getFirst().getY());
	}	
	
	public Position getPosPerso()
	{
		return FilePos.getFirst();
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public void setPosPerso(Position pos)
	{
		FilePos.getFirst().setPosition(pos.getX(), pos.getY());
	}
	
	public Position getAnciennePosPerso()
	{
		return FilePos.get(1);
	}
	
	public void setDirectionPerso(Direction d)
	{
		direction=d;
	}
	
	public void setVitesse(int v)
	{
		vitesseDeplacement = v;
	}
	
	public int getVitesse()
	{
		return vitesseDeplacement;
	}
	
	public void run()
	{
		
		while(true)
		{
			Deplacer();
			setChanged();
			notifyObservers();
			try
			{
				Thread.sleep(vitesseDeplacement);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
