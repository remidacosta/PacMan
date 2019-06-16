package pacman;
import java.lang.Math;
import java.util.LinkedList;

import lib.Direction;
import lib.Position;

public class FantomeRouge extends Fantome
{

	public FantomeRouge() // A supprimer
	{
		super();
		cible=new Position(1,1);
		FilePos.getFirst().setPosition(10,9);
	}

	public FantomeRouge(TerrainPacman terrain)
	{
		super();
		cible=new Position(1,1);
		FilePos=new LinkedList<Position>();
		FilePos.add(new Position(10,9));
		FilePos.add(1, new Position(FilePos.getFirst().getX(),FilePos.getFirst().getY()));
		this.terrain=terrain;		
	}
	
	protected void suivre()
	{/*
		this.setCible(terrain.getPersonnages().get(0).getPosPerso()); // A modifier ??
		Object [] tab=DistanceCible(positionActuelle,terrain,0);
		Position tmp = new Position(positionActuelle.getX(),positionActuelle.getY());
		tmp.setPosition((Direction)tab[1]);
		if(!(terrain.getCase(tmp) instanceof Mur) && !(cible.equals(positionActuelle)))
		{
			direction=(Direction)tab[1];
			anciennePosition.setPosition(positionActuelle.getX(),positionActuelle.getY());
			positionActuelle.setPosition(direction);
		}*/
		if(CaseVoitPacman(FilePos.getFirst().getX(),FilePos.getFirst().getY()))
		{
			Direction next=Direction.haut;
			int min=100;
			for(Direction d: Direction.values())
			{
				Position tmp = FilePos.getFirst().clone();
				tmp.setPosition(d);
				if(CaseVoitPacman(tmp.getX(),tmp.getY()))
				{
					if(positionPile(tmp.getX(),tmp.getY())<min)
					{
						min=positionPile(tmp.getX(),tmp.getY());
						next = d;
					}
					
				}
			}
			direction = next;
			Position tmp = FilePos.getFirst().clone();
			tmp.setPosition(next);
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
				if(!(terrain.getCase(tmp) instanceof Mur))
				FilePos.getFirst().setPosition(direction);
			}
		}
		else
		{
			suivreAleatoire();
		}	
		
	}
	/*
	private Object [] DistanceCible(Position pos,Terrain terrain,int cpt)
	{	
		if(cible.equals(pos))
		{
			Object [] tab= {0,Direction.haut}; 
			return tab;
		}
		else if(pos.getX()==0 || pos.getX()==terrain.getDimX()) { Object [] tab={200,Direction.haut}; return tab ; }
		else if(pos.getY()==0 || pos.getY()==terrain.getDimY()) { Object [] tab={200,Direction.haut}; return tab ; }
		else if(cpt>25) { Object [] tab={50,Direction.haut}; return tab ; }
		else if(terrain.getCase(pos) instanceof Mur) { Object [] tab={200,Direction.haut}; return tab ; }
		else {
			Position bas=pos.clone(); bas.setPosition(Direction.bas);
			Position haut=pos.clone(); haut.setPosition(Direction.haut);
			Position droite=pos.clone(); droite.setPosition(Direction.droite);
			Position gauche=pos.clone(); gauche.setPosition(Direction.gauche);
			Terrain tmp= terrain.clone();
			tmp.setCaseMur(pos);
			Object [] B=DistanceCible(bas,tmp,cpt+1);
			Object[] H=DistanceCible(haut,tmp,cpt+1);
			Object []G=DistanceCible(gauche,tmp,cpt+1);
			Object [] D=DistanceCible(droite,tmp,cpt+1);
			int min=Math.min((int)B[0],Math.min((int)H[0],Math.min((int)D[0],(int)G[0])));
			//int bestX=0,bestY=0;
			Direction d=Direction.haut;
			//récupère le meilleur coup
			if((int)B[0]==min) { d=Direction.bas; }
			else if((int)H[0]==min) { d=Direction.haut; }
			else if((int)D[0]==min) { d=Direction.droite; }
			else if((int)G[0]==min) { d=Direction.gauche; }
			Object [] tab = {min+1,d}; 
			return tab;
		}
	} */

}

