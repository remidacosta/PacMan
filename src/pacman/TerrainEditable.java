package pacman;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;

import lib.Objet;
import lib.Position;


public class TerrainEditable extends Observable
{
	TerrainPacman terrain; 
	Position derniere; // derniere case changée
	String nomFichier;
	public TerrainEditable()
	{
		nomFichier = "test";
		terrain= new TerrainPacman("./images/terrains/modeles_standards/modelterrain-editable.png"); // A modif avec le bon modèle
		derniere =null;
	}
		
	public void sauvegarder()
	{
			int valRGB = 0;
			int r = 0; int g = 0; int b = 0;
			try
			{
				BufferedImage img = new BufferedImage(terrain.getDimY(),terrain.getDimX(), BufferedImage.TYPE_INT_RGB);
				BufferedImage imgRendu = new BufferedImage(terrain.getDimY()*10,terrain.getDimX()*10, BufferedImage.TYPE_INT_RGB);
				for(int i=1; i<=terrain.getDimY();i++)
				{
					for(int j=1; j<=terrain.getDimX();j++)
					{
						if(terrain.getCase(new Position(j-1,i-1)) instanceof Mur)
						{
							r =63; g =72; b =204;
						}
						else if(terrain.getCase(new Position(j-1,i-1)) instanceof Teleporteur)
						{
							r =237; g =28; b =36;
						}
						else if(terrain.getCase(new Position(j-1,i-1)) instanceof Chemin && ((Chemin)terrain.getCase(new Position(j-1,i-1))).getObjet() == null)
						{
							r =34; g =177; b =76;
						}
						else if(terrain.getCase(new Position(j-1,i-1)) instanceof Chemin && ((Chemin)terrain.getCase(new Position(j-1,i-1))).getObjet() instanceof SuperPastille)
						{
							r =0; g =0; b =0;
						}
						else if(terrain.getCase(new Position(j-1,i-1)) instanceof Chemin && ((Chemin)terrain.getCase(new Position(j-1,i-1))).getObjet() instanceof Pastille)
						{
							r =255; g =255; b =255;
						}
						
						
						valRGB = (r << 16) | (g << 8) | b;
						
						img.setRGB((i-1), (j-1), valRGB);
						
						int valX = (j-1)*10;
						int valY = (i-1)*10;
						for(int k=valX; k<valX + 10 ;k++)
						{
							for(int h=valY;h<valY + 10;h++)
							{
								imgRendu.setRGB(h, k, valRGB);
							}
						}
						
					}
				}
				
				ImageIO.write(img,"png",new File("images/terrains/sauvegardes/modeles/"+nomFichier+".png"));	
				ImageIO.write(imgRendu,"png",new File("images/terrains/sauvegardes/rendus/"+nomFichier+".png"));	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
	}
	
	private boolean caseDepart(int x,int y)
	{
		return(x == 16 && y == 9);
	}
	
	private boolean casesFantomes(int x,int y)
	{
		return((x == 10 && y == 8) || (x == 10 && y == 9) ||
			   (x == 10 && y == 10) || (x == 9 && y == 9) ||
			   (x == 8 && y == 8) || (x == 8 && y == 9) || (x == 8 && y == 10));
	}
	
	private boolean caseContourTerrain(int x,int y)
	{
		return(x == 0 || y == 0 || x == terrain.getDimX()-1 || y == terrain.getDimY()-1);
	}
	
	public void setType(int x, int y)
	{
		if(!caseDepart(x,y) && !casesFantomes(x,y))
		{
			switch(terrain.getCase(x,y).getClass().getSimpleName()) 
			{
				case "Mur":
					if(caseContourTerrain(x,y))
					{
						Teleporteur tp = new Teleporteur(new Position(y,x), Teleporteur.nbCaseTeleportation);
						terrain.setCase(x,y,tp);
						terrain.listeTeleport.add(tp);
					}
					else
					{
						terrain.setCase(x,y,new Chemin(new Position(y,x)));
					}
					break;
					
				case "Chemin":
					Teleporteur tp = new Teleporteur(new Position(y,x), Teleporteur.nbCaseTeleportation);
					terrain.setCase(x,y,tp);
					terrain.listeTeleport.add(tp);
		           	break;
		           	
				case "Teleporteur":
					terrain.setCase(x,y,new Mur(new Position(y,x)));
					break;
					
				default:
					break;
			}
			derniere = new Position(x,y);
			setChanged();
			notifyObservers();
		}
	}
	
	public void setNomFichier(String nomFichier)
	{
		this.nomFichier = nomFichier;
	}
	
	public void setPastille(int x, int y)
	{
		switch(terrain.getCase(x,y).getClass().getSimpleName()) 
		{
			case "Chemin":
				Chemin cheminTmp = (Chemin)terrain.getCase(x,y);
				if(cheminTmp.getObjet()==null)
				{
					Objet obj = new Pastille();
					terrain.setCase(x,y,new Chemin(new Position(y,x),obj));
				}
				else
				if(cheminTmp.getObjet() instanceof SuperPastille)
				{
					Objet obj = new Pastille();
					terrain.setCase(x,y,new Chemin(new Position(y,x),obj));
				}
				else
				if(cheminTmp.getObjet() instanceof Pastille)
				{
					Objet obj = new SuperPastille();
					terrain.setCase(x,y,new Chemin(new Position(y,x),obj));
				}
				
           	break;
           	default:
           	break;
		}
		derniere = new Position(x,y);
		setChanged();
		notifyObservers();
	}
	
	public TerrainPacman getTerrain()
	{
		return terrain;
	}
	
	public Position getDerniere()
	{
		return derniere;
	}
	
}
