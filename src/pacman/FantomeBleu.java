package pacman;

import java.util.LinkedList;

import lib.Direction;
import lib.Position;

public class FantomeBleu extends Fantome 
{
	public FantomeBleu() // A supprimer
	{
		super();	
	}
	
	public FantomeBleu(TerrainPacman terrain)
	{
		super();
		this.terrain=terrain;
		FilePos=new LinkedList<Position>();
		FilePos.add(new Position(10,9));
		FilePos.add(1, new Position(FilePos.getFirst().getX(),FilePos.getFirst().getY()));
		cible=new Position(0,0);
	}
		
	public void suivre()
	{
		suivreAleatoire();
	}	
}
