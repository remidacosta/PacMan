package pacman;

import lib.Case;
import lib.Direction;
import lib.Position;

public class PortalGun extends Pouvoir
{
	PortalGun()
	{
		super();
	}
	
	private Case caseMurDansDir(TerrainPacman terrain, Position pos, Direction dir)
	{		
		Position postmp = pos.clone();
		do
		{			
			postmp.setPosition(dir);
			
		}while(terrain.getCase(postmp) instanceof Chemin);
		
		System.out.println("x : "+postmp.getX()+" -  y : "+postmp.getY());
		return terrain.getCase(postmp);
	}
	
	public boolean utiliser(Pacman pac, TerrainPacman terrain)
	{
		terrain.setCaseTeleportation(caseMurDansDir(terrain , pac.getPosPerso() , pac.getDirection()).getPosition() , pac.getDirection().getOpposee());
		return true;
	}
}
