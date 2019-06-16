package pacman;

import lib.Objet;

public abstract class Pouvoir extends Objet
{
	protected boolean actif;
	
	Pouvoir()
	{
		actif = false;
		nbPoints = 80;
	}
	
	protected void setActif(boolean actif)
	{
		this.actif = actif;
	}
	
	public boolean getActif()
	{
		return actif;
	}
	
	public abstract boolean utiliser(Pacman pac, TerrainPacman terrain); 
}
