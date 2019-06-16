package pacman;

import java.util.LinkedList;

import lib.Compteur;
import lib.Direction;
import lib.Position;

public class Pacman extends Perso
{
	private int score;
	private Pouvoir pouvoir = null;
	private Direction prochaineDirection;
	public Compteur compt;
	
	public Pacman(TerrainPacman terrain)
	{
		vie = 3;
		predateur = false;
		score = 0;
		FilePos=new LinkedList<Position>();
		FilePos.add(new Position(16,9));
		FilePos.add(1, new Position(FilePos.getFirst().getX(),FilePos.getFirst().getY()));
		direction = Direction.haut;
		prochaineDirection = Direction.haut;
		
		
		this.terrain=terrain;
		compt = new Compteur(1,2,1);
	}
	
	private boolean checkContacts()
	{
		for(int i=0; i<terrain.getEntites().size();i++)
		{
			if(this != terrain.getEntites().get(i))
			{
				Perso p2 = (Perso)terrain.getEntites().get(i);
				if(this.estPositionPerso(p2.getPosPerso().getX(), p2.getPosPerso().getY()) ||
				     (p2.estPositionPerso(this.getAnciennePosPerso().getX(), this.getAnciennePosPerso().getY()) &&
				      p2.getDirection()==this.getDirection().getOpposee()) )
					{
						if(p2.getSiPredateur())
						{
							this.setVie(this.getVie()-1);
							direction = Direction.haut;
							prochaineDirection = Direction.haut;
							compt.incremente();
							Position tmp = new Position (FilePos.getFirst().getX(),FilePos.getFirst().getY());
							FilePos.clear();
							FilePos.addFirst(tmp);
							FilePos.addFirst(new Position(16,9));
							
							for(int j=1; j<terrain.getEntites().size();j++)
							{
								((Fantome)terrain.getEntites().get(j)).setVie(0);
							}
							
						}
						else
						{
							p2.setVie(p2.getVie()-1);
						}
						
						return true;
					}
			}
		}
		return false;
	}
	
	public void Deplacer()
	{
		checkContacts();
		if(this.getSiPredateur()) predateur=false;
		checkObjets();
		//Si le pacman peut effectuer la dernière touche appuyée, il l'effectue
		Position t=FilePos.getFirst().clone();
		t.setPosition(prochaineDirection);
		if(terrain.getCase(t) instanceof Chemin || terrain.getCase(t) instanceof Teleporteur)
		{
			direction=prochaineDirection;
		}
			
		// on regarde si c'est une case de teleportation
		Position tmp=FilePos.getFirst().clone();
		tmp.setPosition(direction);
		Position tmp2=FilePos.getFirst().clone();
		if(terrain.getCase(tmp) instanceof Teleporteur)
		{
			
			Teleporteur tpEntree = (Teleporteur)terrain.getCase(tmp);
			Teleporteur tpSortie = terrain.getTeleporteur(tpEntree.getId(),tpEntree.getPosition());
			
			if(tpSortie != null)
			{
				FilePos.addFirst(tmp.clone());
				FilePos.addFirst(tmp2.clone());
				FilePos.addFirst(new Position(tpSortie.getPosition().getX() + tpSortie.getDirSortie().getX(),
											 tpSortie.getPosition().getY() + tpSortie.getDirSortie().getY()));
				direction = tpSortie.getDirSortie();
				prochaineDirection = tpSortie.getDirSortie();
				if(FilePos.size()>15) FilePos.removeLast();
			}
			
		}
		
		if(pouvoir != null && pouvoir.getActif())
		{
			if(utiliserPouvoir(terrain)) pouvoir = null;
		}
				
		if(terrain.getCase(tmp) instanceof Chemin)
		{
			compt.incremente();
			FilePos.addFirst(tmp.clone());
			if(FilePos.size()>15) FilePos.removeLast();
		}
		
	}
	
	public void activerPouvoir()
	{
		if(pouvoir != null)
		{
			pouvoir.setActif(true);
		}
	}
	
	public void desactiverPouvoir()
	{
		if(pouvoir != null)
		{
			pouvoir.setActif(false);
		}
	}
	
	public boolean pouvoirEstActif()
	{
		return pouvoir.getActif();
	}
	
	private boolean utiliserPouvoir(TerrainPacman ter)
	{
		return(pouvoir.utiliser(this, ter));
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public Direction getProchaineDir()
	{
		return prochaineDirection;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public int getVie()
	{
		return vie;
	}
	
	public void setProchaineDirection(Direction suivante)
	{
		prochaineDirection=suivante;
	}
	
	public void setTerrain(TerrainPacman ter)
	{
		terrain = ter;
	}
	
	public void checkObjets()
	{	
		// on regarde d'abord si la position actuelle contient quelque chose
		if(terrain.getCase(FilePos.getFirst()) instanceof Chemin)
		{
			Chemin cheminTmp = (Chemin)terrain.getCase(FilePos.getFirst());
			if(cheminTmp.getObjet() != null)
			{
				if(cheminTmp.getObjet() instanceof SuperPastille)
				{
					predateur = true;
					for(int i=1; i<terrain.getEntites().size();i++)
					{
						((Perso)terrain.getEntites().get(i)).setPredateur(false);
					}
				}
				else if(cheminTmp.getObjet() instanceof Pastille)
				{
					terrain.diminuerNbPastilles();
				}
				else if(cheminTmp.getObjet() instanceof Pouvoir)
				{
					pouvoir = (Pouvoir)cheminTmp.getObjet();
				}
				
				score = score + cheminTmp.getObjet().getNbPoints();
				terrain.setCaseChemin(FilePos.getFirst());
			}
		}
	}
	
	public LinkedList<Position> getFile()
	{
		return FilePos;
	}
	
	public Pouvoir getPouvoir()
	{
		return pouvoir;
	}
}
