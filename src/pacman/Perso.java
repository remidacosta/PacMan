package pacman;
import java.util.LinkedList;
import java.util.Observable;

import lib.Direction;
import lib.Entite;
import lib.Position;

public abstract class Perso extends Entite
{
	protected TerrainPacman terrain;
	protected boolean predateur;
	protected int vie;
	
	public boolean getSiPredateur()
	{
		return predateur;
	}
	
	public void setPredateur(boolean predateur)
	{
		this.predateur = predateur;
	}
	
	public int getVie()
	{
		return vie;
	}
	
	public void setVie(int vie)
	{
		this.vie = vie;
	}
}
