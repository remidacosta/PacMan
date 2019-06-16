package pacman;

import lib.Case;
import lib.Direction;
import lib.Position;

public class PasseMur extends Pouvoir
{
	PasseMur()
	{
		super();
	}
	
	private Case caseLibreDansDir(TerrainPacman terrain, Position pos, Direction dir)
	{		
		Position postmp = pos.clone();
		int compt = 0;
		do
		{
			compt++;
			postmp.setPosition(dir);
			
		}while(terrain.getCase(postmp) instanceof Mur);
		
		if(compt >4 || postmp.getX() <1 || postmp.getX()>(terrain.getDimX()-1) || postmp.getY() <1 || postmp.getY()>(terrain.getDimY()-1))
		{
			return terrain.getCase(pos);
		}
		else
		{
			return terrain.getCase(postmp);
		}
	}
	
	public boolean utiliser(Pacman pac, TerrainPacman terrain)
	{
		Position tmp = pac.getPosPerso().clone();
		tmp.setPosition(pac.getDirection());
		
		if(terrain.getCase(tmp) instanceof Mur &&
		   !(pac.getPosPerso().getX() == 1 && pac.getDirection() == Direction.haut) &&
		   !(pac.getPosPerso().getY() == 1 && pac.getDirection() == Direction.gauche) &&
		   !(pac.getPosPerso().getX() == (terrain.getDimX()-2) && pac.getDirection() == Direction.bas) &&
		   !(pac.getPosPerso().getY() == (terrain.getDimY()-2) && pac.getDirection() == Direction.droite))
		{
			pac.getFile().addFirst(caseLibreDansDir(terrain, pac.getPosPerso(), pac.getProchaineDir()).getPosition());
			return true;
		}
		else
		{
			return false;
		}

	}
}
