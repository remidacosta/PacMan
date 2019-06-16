package pacman;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import javax.imageio.ImageIO;

import lib.Case;
import lib.Direction;
import lib.Terrain;
import lib.Objet;
import lib.Position;
import lib.Entite;


public class TerrainPacman extends Terrain implements Cloneable
{
	protected int nbJoueurs;
	protected int nbPastillesRest = 0;
	protected ArrayList<Teleporteur> listeTeleport;
	protected Case caseChange;
	protected String chemin;
	
	public TerrainPacman()
	{
		this(1,"images/terrains/modeles_standards/modelterrain-standard.bmp");
	}
	
	public TerrainPacman(String chemin)
	{
		this(1,chemin);
	}
	
	public TerrainPacman(int nbJ)
	{
		this(nbJ,"images/terrains/modeles_standards/modelterrain-standard.bmp");
	}
	
	public TerrainPacman(int nbJ, String chemin)
	{
		tabEntites= new ArrayList<Entite>();
		listeTeleport = new ArrayList<Teleporteur>();
		
		nbJoueurs = nbJ;
		this.chemin = chemin;
		
		remplirTabCases();
		setDirSortieTeleport();
	}
	
	public Teleporteur getTeleporteur(int id, Position posTelActuel)
	{
		int compt = 0;
		if(listeTeleport.size() == 1)
		{
			return null;
		}
		
		for(int i=0;i<listeTeleport.size();i++)
		{
			if(listeTeleport.get(i).getPosition() != posTelActuel && id != listeTeleport.get(i).getId() && listeTeleport.get(i).getAccessible())
			{
				compt++;
			}
		}
		
		if(compt != 0)
		{
			Random rand = new Random();
			int teleporteurAlea;
			do
			{
				teleporteurAlea = rand.nextInt(listeTeleport.size());
			}while(listeTeleport.get(teleporteurAlea).getId() == id || !listeTeleport.get(teleporteurAlea).getAccessible());
			
			return listeTeleport.get(teleporteurAlea);
		}
		else return null;
		
	}
	
	private boolean caseEstLibre(int numL, int numC)
	{
		return(tabCases[numL][numC] instanceof Chemin &&
			   !(((Chemin)tabCases[numL][numC]).getObjet() instanceof Pastille) &&
			   !(((Chemin)tabCases[numL][numC]).getObjet() instanceof SuperPastille));
	}
	
	public void genererPouvoir()
	{
		Random rand = new Random();
		
		int tabX[] = {8,9,10,11,12};
		int tabY[] = {6,7,8,9,10,11,12};
		int numLigne;
		int numColonne;
		
		do
		{
			numLigne = rand.nextInt(tabX.length) + tabX[0];
			if(numLigne == 8 || numLigne == 12)
			{
				numColonne = rand.nextInt(tabY.length) + tabY[0];
			}
			else
			{
				numColonne = tabY[ rand.nextInt(2)*(tabY.length-1) ];
			}
		}while(!caseEstLibre(numLigne,numColonne));
		
		int typePouvoir = rand.nextInt(2);
		switch(typePouvoir)
		{
			case 0:
				((Chemin)tabCases[numLigne][numColonne]).setObjet(new PasseMur());
				break;
				
			case 1:
				((Chemin)tabCases[numLigne][numColonne]).setObjet(new PortalGun());
				break;
				
			default:
				((Chemin)tabCases[numLigne][numColonne]).setObjet(new PasseMur());
				break;
		}
		
		caseChange = tabCases[numLigne][numColonne];
		setChanged();
   	 	notifyObservers();
	}
	
	private void remplirTabCases()
	{
		try
		{
			BufferedImage image = ImageIO.read(new File(chemin));	// je lis l'image se trouvant dans ce fichier
		    Color[][] colors = new Color[image.getWidth()][image.getHeight()];			// je crée un tableau de couleurs
		    
		    dimX = image.getHeight();
		    dimY = image.getWidth();
		    tabCases = new Case[dimX][dimY];					// j'initialise mon tableau de cases
		    
		     for (int x = 0; x < image.getWidth(); x++)
		     {
		    	 for (int y = 0; y < image.getHeight(); y++)
		    	 {
		             colors[x][y] = new Color(image.getRGB(x, y));						// je regarde la couleur du pixel de l'image
		             //System.out.println(colors[x][y].toString());
		             
		             if(colors[x][y].equals( new Color(63,72,204)))  //Viollet				si c'est viollet c'est un mur
		             {
		            	 tabCases[y][x] = new Mur(new Position(y,x));					// la case devient donc un mur
		             }
		             else if(colors[x][y].equals( new Color(255,255,255))) //Blanc			si c'est blanc c'est une case de chemin avec une pastille
		             {
		            	 nbPastillesRest++;
		            	 Objet obj = new Pastille();
		            	 tabCases[y][x] = new Chemin(new Position(y,x),obj);
		             }
		             else if(colors[x][y].equals( new Color(0,0,0)))  //Noir				si c'est noir c'est une case chemin avec une superpastille
		             {
		            	 Objet obj = new SuperPastille();
		            	 tabCases[y][x] = new Chemin(new Position(y,x),obj);
		             }
		             else if(colors[x][y].equals( new Color(34,177,76))) //Vert				si c'est vert c'est une case chemin sans objet
		             {
		            	 tabCases[y][x] = new Chemin(new Position(y,x));
		             }
		             else if(colors[x][y].equals( new Color(237,28,36)))  //Rouge			si c'est rouge c'est une case de teleportation
		             {
		            	 Teleporteur tp = new Teleporteur(new Position(y,x), Teleporteur.nbCaseTeleportation);
		            	 tabCases[y][x] = tp;
		            	 listeTeleport.add(tp);
		             }
		         }
		     }
		     
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	protected void setDirSortieTeleport()
	{
		ArrayList<Direction> casesAdjLib;
		for(int i=0; i<listeTeleport.size();i++)
		{
			casesAdjLib= new ArrayList<Direction>();
			
			if(listeTeleport.get(i).getPosition().getX() == 0)
			{
				listeTeleport.get(i).setDirSortie(Direction.bas);
			}
			else if(listeTeleport.get(i).getPosition().getX() == (tabCases.length - 1))
			{
				listeTeleport.get(i).setDirSortie(Direction.haut);
			}
			else if(listeTeleport.get(i).getPosition().getY() == 0)
			{
				listeTeleport.get(i).setDirSortie(Direction.droite);
			}
			else if(listeTeleport.get(i).getPosition().getY() == (tabCases[i].length -1))
			{
				listeTeleport.get(i).setDirSortie(Direction.gauche);
			}
			else
			{
				for(int j=-1; j<=1; j= j+2)
				{
					if(tabCases[listeTeleport.get(i).getPosition().getX() + j][listeTeleport.get(i).getPosition().getY()] instanceof Chemin)
					{
						if(j == -1)
						{
							casesAdjLib.add(Direction.haut);
						}
						else if(j == 1)
						{
							casesAdjLib.add(Direction.bas);
						}
						
					}
					if(tabCases[listeTeleport.get(i).getPosition().getX()][listeTeleport.get(i).getPosition().getY() + j] instanceof Chemin)
					{
						if(j == -1)
						{
							casesAdjLib.add(Direction.gauche);
						}
						else if(j == 1)
						{
							casesAdjLib.add(Direction.droite);
						}
					}
				}
				
				if(casesAdjLib.size() == 0 || casesAdjLib.size() == 4)
				{
					listeTeleport.get(i).setDirSortie(Direction.haut);
					listeTeleport.get(i).setAccessible(false);
				}
				else
				{
					Random rand = new Random();
					int aleaDirection = rand.nextInt(casesAdjLib.size());
					
					listeTeleport.get(i).setDirSortie(casesAdjLib.get(aleaDirection));
				}
			}
		}
	}
	
	protected void remplirLigneDeMurs(Position posDeb, Position posFin)
	{
		for(int i=posDeb.getX(); i<=posFin.getX(); i++)
		{
			for(int j=posDeb.getY(); j<=posFin.getY();j++)
			{
				tabCases[i][j] = new Mur(new Position(i,j));
			}
		}
	}
	
	public int getNbPastillesRest()
	{
		return nbPastillesRest;
	}
	
	public void diminuerNbPastilles()
	{
		nbPastillesRest--;
	}
	
	public boolean positionDansTerrain(Position pos, int ecart)
	{
		return(pos.getX() >= ecart && pos.getY() >= ecart && pos.getX() <= (dimX-1-ecart) && pos.getY() <= (dimY-1-ecart));
	}
		
	public Case getCaseChange()
	{
		return caseChange;
	}
	
	public void setCaseMur(Position pos)
	{
		tabCases[pos.getX()][pos.getY()]= new Mur (pos);
	}
	
	public void setCaseChemin(Position pos)
	{
		tabCases[pos.getX()][pos.getY()]= new Chemin (pos);
	}
	
	public void setCaseTeleportation(Position pos, Direction dirSortie)
	{
		Teleporteur tp = new Teleporteur(pos, Teleporteur.nbCaseTeleportation);
		tp.setDirSortie(dirSortie);
   	 	tabCases[pos.getX()][pos.getY()] = tp;
   	 	listeTeleport.add(tp);
   	 	
   	 	caseChange = tp;
   	 	setChanged();
   	 	notifyObservers();
	}
	
	public int getNbJoueurs()
	{
		return nbJoueurs;
	}
	
	public void finPredateur()
	{
		for(int i=1; i<tabEntites.size();i++)
		{
			((Perso)tabEntites.get(i)).setPredateur(true);
		}
	}
	
	public String getChemin()
	{
		return chemin;
	}
	
	public void setCase(int x, int y,Case c)
	{
		tabCases[x][y]=c;	
	}
}
