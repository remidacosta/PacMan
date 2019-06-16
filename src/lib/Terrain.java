package lib;

import java.util.ArrayList;
import java.util.Observable;

import pacman.Perso;

public class Terrain extends Observable
{
	protected int dimX;
	protected int dimY;
	protected Case tabCases[][];
	protected ArrayList<Entite> tabEntites;
	
	public int getDimX()
	{
		return dimX;
	}
	
	public int getDimY()
	{
		return dimY;
	}
	
	public ArrayList<Entite> getEntites()
	{
		return tabEntites;
	}
	
	public void setEntites(ArrayList<Entite> Entites)
	{
		this.tabEntites=Entites;
	}
	
	public Case getCase(Position pos)
	{
		return tabCases[pos.getX()][pos.getY()];
	}
	
	public Terrain clone()
	  { 
		  Terrain obj=null;
	    try
	    { 
	      obj=(Terrain)super.clone();
	      
	    } 
	    catch (CloneNotSupportedException x)
	    {
	      x.printStackTrace(System.err);
	    }
	    obj.tabCases=(Case[][])tabCases.clone();
	    obj.tabCases= new Case [dimX][dimY];
	    for(int i=0;i<dimX;i++)
	    {
	    	for(int j=0;j<dimY;j++)
	    	{
	    		obj.tabCases[i][j]=(Case)tabCases[i][j].clone();
	    	}
	    }
	    
	    return obj;
	}
	
	public Case getCase(int x, int y)
	{
		return tabCases[x][y];
	}
	
}

