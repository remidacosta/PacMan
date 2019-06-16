package pacman;

import lib.Direction;
import lib.Position;

public abstract class Fantome extends Perso
{
	protected Position cible;
	
	Fantome()
	{
		vie = 1;
		predateur = true;
		direction=Direction.haut;
	}
		
	public void setCible(Position cible)
	{
		this.cible.setPosition(cible.getX(), cible.getY());
	}

	public void renaissance()
	{
		this.vie = 1;
		FilePos.get(1).setPosition(FilePos.getFirst().getX(),FilePos.getFirst().getY());
		this.FilePos.getFirst().setPosition(10, 9);
		
		this.setPredateur(true);
	}
	public void Deplacer() 
	{
		if(vie == 0)
		{
			renaissance();
		}
		else
		{
			if(predateur == true)
			{
				suivre();
			}
			else
			{
				fuir();
			}
		}
		
	}
	
	protected abstract void suivre();
	
	public void fuir()
	{
		Position futur= FilePos.getFirst().clone();
		if(videAutour(terrain)>2)
		{
			direction=Direction.DirectionAleatoire();
		}
		
		futur.setPosition(direction);
		Direction tmp=direction;
		while((tmp==terrain.getEntites().get(0).getDirection().getOpposee()&&videAutour(terrain)>1)
				|| terrain.getCase(futur) instanceof Mur)
		{
			futur=FilePos.getFirst().clone();
			tmp=Direction.DirectionAleatoire();
			futur.setPosition(tmp);
		}
		direction=tmp;
		FilePos.get(1).setPosition(FilePos.getFirst().getX(),FilePos.getFirst().getY());
		
		if(terrain.getCase(futur) instanceof Teleporteur)// Un fantome peut passer dans un teleporteur
		{
			Teleporteur tpEntree = (Teleporteur)terrain.getCase(futur);
			Teleporteur tpSortie = terrain.getTeleporteur(tpEntree.getId(),tpEntree.getPosition());
			FilePos.getFirst().setPosition(tpSortie.getPosition().getX() + tpSortie.getDirSortie().getX(),
					 					 tpSortie.getPosition().getY() + tpSortie.getDirSortie().getY());
			direction = tpSortie.getDirSortie();
		}
		else
		{
			FilePos.getFirst().setPosition(direction);
		}
	}

	public int videAutour(TerrainPacman terrain)
	{
		int nb=0;
		for(Direction d : Direction.values())
		{
			Position tmp=FilePos.getFirst().clone();
			tmp.setPosition(d);
			if(!(terrain.getCase(tmp) instanceof Mur))
			{
				nb++;
			}
		}
		return nb;
	}
	
	public boolean CaseVoitPacman(int x, int y)
	{
		if(((Pacman)(terrain.getEntites().get(0))).getFile().size()>2)
		{
			for(Position p: ((Pacman)(terrain.getEntites().get(0))).getFile())
			{
				if(x==p.getX() && y==p.getY())
				{
					return true;
				}
			}
		}
		return false;
	}
		
	public int positionPile(int x, int y)
	{
		int i=0;
		for(Position p: ((Pacman)(terrain.getEntites().get(0))).getFile())
		{
			if(x==p.getX() && y==p.getY())
			{
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public void suivreAleatoire()
	{
		Position tmp= FilePos.getFirst().clone();
		if(videAutour(terrain)>2)// permet au fantome de changer de direction lorsqu'il y a 3-4 passages possibles
		{
			direction=Direction.DirectionAleatoire();
		}
		
		tmp.setPosition(direction);
		while(terrain.getCase(tmp) instanceof Mur)
		{
			tmp=FilePos.getFirst().clone();
			direction=Direction.DirectionAleatoire();
			if(FilePos.getFirst().getX()==10&&FilePos.getFirst().getY()==9) {direction=Direction.haut;} // A ameliorer
			tmp.setPosition(direction);
		}
		
		FilePos.get(1).setPosition(FilePos.getFirst().getX(),FilePos.getFirst().getY());
		
		if(terrain.getCase(tmp) instanceof Teleporteur)		// Un fantome peut passer dans un teleporteur
		{
			Teleporteur tpEntree = (Teleporteur)terrain.getCase(tmp);
			Teleporteur tpSortie = terrain.getTeleporteur(tpEntree.getId(),tpEntree.getPosition());
			FilePos.getFirst().setPosition(tpSortie.getPosition().getX() + tpSortie.getDirSortie().getX(),
					 					 tpSortie.getPosition().getY() + tpSortie.getDirSortie().getY());
			direction = tpSortie.getDirSortie();
		}
		else
		{
			FilePos.getFirst().setPosition(direction);
		}
	}
}
