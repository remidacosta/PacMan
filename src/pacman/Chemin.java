package pacman;

import lib.Case;
import lib.Objet;
import lib.Position;

public class Chemin extends Case
{
	private Objet obj;
	
	Chemin(Position pos)
	{
		super(pos);
		obj = null;
	}
	
	Chemin(Objet obj)
	{
		super(new Position(0,0));
		this.obj = obj;
	}
	
	Chemin(Position pos, Objet obj)
	{
		super(pos);
		this.obj = obj;
	}
	
	public Objet getObjet()
	{
		return obj;
	}
	
	public void setObjet(Objet obj)
	{
		this.obj = obj;
	}
}
