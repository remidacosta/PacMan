package pacman;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import lib.Case;
import lib.Objet;
import lib.Position;
import lib.Entite;

public class TerrainAleat extends TerrainPacman
{
	private String idGeneration;
	
	TerrainAleat()
	{
		this(1);
	}
	TerrainAleat(int nbJ)
	{
		nbPastillesRest=0;
		System.out.println("nb past init : "+nbPastillesRest);
		tabEntites= new ArrayList<Entite>();
		listeTeleport = new ArrayList<Teleporteur>();
		
		nbJoueurs = nbJ;
		
		remplirTabCases();
		setDirSortieTeleport();
		
		System.out.println("nb past apres init : "+nbPastillesRest);
	}
	
	private void remplirZoneAlea(int indice,int valeur, Position posDeb)
	{
		try
		{
			BufferedImage image = ImageIO.read(new File("images/generationsAleat/modeles/modele-" + indice + "-" + valeur+".png"));	// je lis l'image se trouvant dans ce fichier
		    Color[][] colors = new Color[image.getWidth()][image.getHeight()];			// je crée un tableau de couleurs
		    
		     for (int x = 0; x < image.getWidth(); x++)
		     {
		    	 for (int y = 0; y < image.getHeight(); y++)
		    	 {
		             colors[x][y] = new Color(image.getRGB(x, y));						// je regarde la couleur du pixel de l'image
		             //System.out.println(colors[x][y].toString());
		             
		             if(colors[x][y].equals( new Color(63,72,204)))  //Viollet				si c'est viollet c'est un mur
		             {
		            	 tabCases[y+posDeb.getX()][x+posDeb.getY()] = new Mur(new Position(y+posDeb.getX(),x+posDeb.getY()));					// la case devient donc un mur
		             }
		         }
		     }  
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void remplirTabCases()
	{
		//for(int i=0; i<200;i++) {
		try
		{
			BufferedImage image = ImageIO.read(new File("images/terrains/modeles_standards/modelterrain-aleat.bmp"));	// je lis l'image se trouvant dans ce fichier
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
		             else if(colors[x][y].equals( new Color(0,0,0)))  //Noir				si c'est noir c'est une case chemin avec une superpastille
		             {
		            	 Objet obj = new SuperPastille();
		            	 tabCases[y][x] = new Chemin(new Position(y,x),obj);
		             }
		         }
		     }
		     
		     loisAleatoires();
		     
		     for (int x = 0; x < image.getWidth(); x++)
		     {
		    	 for (int y = 0; y < image.getHeight(); y++)
		    	 {
		             colors[x][y] = new Color(image.getRGB(x, y));						// je regarde la couleur du pixel de l'image
		             //System.out.println(colors[x][y].toString());

		             if(colors[x][y].equals( new Color(255,255,255)) && tabCases[y][x] == null) //Blanc			si c'est blanc c'est une case de chemin avec une pastille
		             {
		            	 nbPastillesRest++;
		            	 Objet obj = new Pastille();
		            	 tabCases[y][x] = new Chemin(new Position(y,x),obj);
		             }
		             else if(colors[x][y].equals( new Color(34,177,76)) && tabCases[y][x] == null) //Vert				si c'est vert c'est une case chemin sans objet
		             {
		            	 tabCases[y][x] = new Chemin(new Position(y,x));
		             }
		         }
		     }
		     
		     //sauvegarderGeneration();
		     
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//}
	}
	
	private void sauvegarderGeneration()
	{
		int valRGB = 0;
		int r = 0; int g = 0; int b = 0;
		try
		{
			BufferedImage img = new BufferedImage(dimY*10, dimX*10, BufferedImage.TYPE_INT_RGB);
			for(int i=1; i<=dimY;i++)
			{
				for(int j=1; j<=dimX;j++)
				{
					if(tabCases[j-1][i-1] instanceof Mur)
					{
						r =63; g =72; b =204;
					}
					else if(tabCases[j-1][i-1] instanceof Teleporteur)
					{
						r =237; g =28; b =36;
					}
					else if(tabCases[j-1][i-1] instanceof Chemin && ((Chemin)tabCases[j-1][i-1]).getObjet() == null)
					{
						r =255; g =255; b =255;
					}
					else if(tabCases[j-1][i-1] instanceof Chemin && ((Chemin)tabCases[j-1][i-1]).getObjet() instanceof Pastille)
					{
						r =255; g =255; b =255;
					}
					else if(tabCases[j-1][i-1] instanceof Chemin && ((Chemin)tabCases[j-1][i-1]).getObjet() instanceof SuperPastille)
					{
						r =0; g =0; b =0;
					}
					
					valRGB = (r << 16) | (g << 8) | b;
					
					int valX = (j-1)*10;
					int valY = (i-1)*10;
					for(int k=valX; k<valX + 10 ;k++)
					{
						for(int h=valY;h<valY + 10;h++)
						{
							img.setRGB(h, k, valRGB);
						}
					}
					
				}
			}
			
			ImageIO.write(img,"png",new File("images/generationsaleat/img"+idGeneration+".png"));	
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void loisAleatoires()
	{
		Random rand = new Random();
		// nom des varaibles aleatoires relatif au schema excel en aide
		
		int aleaPorte1 = rand.nextInt(4) + 1; 	// rouge
		int aleaPorte2 = rand.nextInt(3) + 1;	// gris
		int aleaPorte3 = rand.nextInt(3) + 1;	// orange
		// 36 possibilités de générations des portes
		
		int tabAlea[][] = {{0,5},{0,5},{0,5},{0,5},{0,5},{0,5},{0,5},{0,5},{0,5},{0,5}}; // {x,y} : x pour la valeur aleatoire, y pour le maximum possible de la valeur aleatoire
		Position tabPosZones[] = {new Position(2,1) , new Position(2,15), new Position(7,1),
								  new Position(11,1), new Position(7,15), new Position(11,15),
								  new Position(15,1), new Position(15,15), new Position(15,5),
								  new Position(15,10)};
		// 351 562 500 générations possibles avec ce modèle de génération de terrain (36 * 5^10)
		
		int aleaTelG = rand.nextInt(3) + 1; 	// teleporteur mur gauche
		int aleaTelHG = rand.nextInt(8) + 1;	// teleporteur coin haut gauche
		int aleaTelBG = rand.nextInt(8) + 1;	// teleporteur coin bas gauche
		int aleaTelHD = rand.nextInt(8) + 1;	// teleporteur coin haut droit
		int aleaTelBD = rand.nextInt(8) + 1;	// teleporteur coin bas droit
		int aleaTelD = rand.nextInt(3) + 1;		// teleporteur mur droit
		
		// 12 960 000 000 000 générations possibles avec ce modèle (en comptant les teleporteurs)
		
		for(int i=0; i<tabAlea.length; i++)
		{
			tabAlea[i][0] = rand.nextInt(tabAlea[i][1]) + 1;
			remplirZoneAlea(i+1,tabAlea[i][0], tabPosZones[i]);
		}
		
		idGeneration = "" + aleaPorte1 + aleaPorte2 + aleaPorte3;
		for(int i=0; i<tabAlea.length;i++)
		{
			idGeneration = idGeneration + tabAlea[i][0];
		}
		idGeneration = idGeneration + aleaTelG + aleaTelHG + aleaTelBG + aleaTelHD + aleaTelBD + aleaTelD;
		
		switch(aleaPorte1)
		{
			case 1:
				tabCases[3][9] = new Mur(new Position(3,9));
				tabCases[4][9] = new Mur(new Position(4,9));
				tabCases[5][9] = new Mur(new Position(5,9));
				remplirLigneDeMurs(new Position(3,5), new Position(3,7));
				remplirLigneDeMurs(new Position(3,11), new Position(3,13));
				break;
				
			case 2:
				tabCases[2][9] = new Mur(new Position(2,9));
				tabCases[4][9] = new Mur(new Position(4,9));
				tabCases[5][9] = new Mur(new Position(5,9));
				remplirLigneDeMurs(new Position(4,5), new Position(4,7));
				remplirLigneDeMurs(new Position(4,11), new Position(4,13));
				break;
				
			case 3:
				tabCases[2][9] = new Mur(new Position(2,9));
				tabCases[3][9] = new Mur(new Position(3,9));
				tabCases[5][9] = new Mur(new Position(5,9));
				remplirLigneDeMurs(new Position(3,5), new Position(3,7));
				remplirLigneDeMurs(new Position(3,11), new Position(3,13));
				break;
				
			case 4:
				tabCases[2][9] = new Mur(new Position(2,9));
				tabCases[3][9] = new Mur(new Position(3,9));
				tabCases[4][9] = new Mur(new Position(4,9));
				remplirLigneDeMurs(new Position(4,5), new Position(4,7));
				remplirLigneDeMurs(new Position(4,11), new Position(4,13));
				break;
			
			default :
				break;
		}
		
		switch(aleaPorte2)
		{
			case 1:
				remplirLigneDeMurs(new Position(10,5), new Position(11,5));
				remplirLigneDeMurs(new Position(9,13), new Position(10,13));
				break;
				
			case 2:
				tabCases[9][5] = new Mur(new Position(9,5));
				tabCases[11][5] = new Mur(new Position(11,5));
				tabCases[9][13] = new Mur(new Position(9,13));
				tabCases[11][13] = new Mur(new Position(11,13));
				break;
				
			case 3:
				remplirLigneDeMurs(new Position(9,5), new Position(10,5));
				remplirLigneDeMurs(new Position(10,13), new Position(11,13));
				break;
				
			default:
				break;
		}
		
		switch(aleaPorte3)
		{
			case 1:
				remplirLigneDeMurs(new Position(19,9), new Position(20,9));
				break;
				
			case 2:
				tabCases[18][9] = new Mur(new Position(18,9));
				tabCases[20][9] = new Mur(new Position(20,9));
				break;
			
			case 3:
				remplirLigneDeMurs(new Position(18,9), new Position(19,9));
				break;
				
			default:
				break;
		}
		
		switch(aleaTelG)
		{
			case 1:
				Teleporteur tpAlea1 = new Teleporteur(new Position(14,0), Teleporteur.nbCaseTeleportation);
	           	tabCases[14][0] = tpAlea1;
	           	listeTeleport.add(tpAlea1);
				break;
				
			case 2:
				Teleporteur tpAlea2 = new Teleporteur(new Position(10,0), Teleporteur.nbCaseTeleportation);
	           	tabCases[10][0] = tpAlea2;
	           	listeTeleport.add(tpAlea2);
				break;
				
			case 3:
				Teleporteur tpAlea3 = new Teleporteur(new Position(6,0), Teleporteur.nbCaseTeleportation);
	           	tabCases[6][0] = tpAlea3;
	           	listeTeleport.add(tpAlea3);
				break;
				
			default:
				break;
		}
		
		switch(aleaTelD)
		{
			case 1:
				Teleporteur tpAlea1 = new Teleporteur(new Position(14,18), Teleporteur.nbCaseTeleportation);
	           	tabCases[14][18] = tpAlea1;
	           	listeTeleport.add(tpAlea1);
				break;
				
			case 2:
				Teleporteur tpAlea2 = new Teleporteur(new Position(10,18), Teleporteur.nbCaseTeleportation);
	           	tabCases[10][18] = tpAlea2;
	           	listeTeleport.add(tpAlea2);
				break;
				
			case 3:
				Teleporteur tpAlea3 = new Teleporteur(new Position(6,18), Teleporteur.nbCaseTeleportation);
	           	tabCases[6][18] = tpAlea3;
	           	listeTeleport.add(tpAlea3);
				break;
				
			default:
				break;
		}
		
		switch(aleaTelHG)
		{
			case 1:
				Teleporteur tpAlea1 = new Teleporteur(new Position(0,2), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][2] = tpAlea1;
	           	listeTeleport.add(tpAlea1);
				break;
				
			case 2:
				Teleporteur tpAlea2 = new Teleporteur(new Position(0,3), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][3] = tpAlea2;
	           	listeTeleport.add(tpAlea2);
				break;
				
			case 3:
				Teleporteur tpAlea3 = new Teleporteur(new Position(0,4), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][4] = tpAlea3;
	           	listeTeleport.add(tpAlea3);
				break;
			
			case 4:
				Teleporteur tpAlea4 = new Teleporteur(new Position(0,5), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][5] = tpAlea4;
	           	listeTeleport.add(tpAlea4);
				break;
			
			case 5:
				Teleporteur tpAlea5 = new Teleporteur(new Position(0,6), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][6] = tpAlea5;
	           	listeTeleport.add(tpAlea5);
				break;
				
			case 6:
				Teleporteur tpAlea6 = new Teleporteur(new Position(0,7), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][7] = tpAlea6;
	           	listeTeleport.add(tpAlea6);
				break;
				
			case 7:
				Teleporteur tpAlea7 = new Teleporteur(new Position(0,8), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][8] = tpAlea7;
	           	listeTeleport.add(tpAlea7);
				break;
				
			default:
				break;
		}
		
		switch(aleaTelBG)
		{
			case 1:
				Teleporteur tpAlea1 = new Teleporteur(new Position(21,2), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][2] = tpAlea1;
	           	listeTeleport.add(tpAlea1);
				break;
				
			case 2:
				Teleporteur tpAlea2 = new Teleporteur(new Position(21,3), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][3] = tpAlea2;
	           	listeTeleport.add(tpAlea2);
				break;
				
			case 3:
				Teleporteur tpAlea3 = new Teleporteur(new Position(21,4), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][4] = tpAlea3;
	           	listeTeleport.add(tpAlea3);
				break;
			
			case 4:
				Teleporteur tpAlea4 = new Teleporteur(new Position(21,5), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][5] = tpAlea4;
	           	listeTeleport.add(tpAlea4);
				break;
			
			case 5:
				Teleporteur tpAlea5 = new Teleporteur(new Position(21,6), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][6] = tpAlea5;
	           	listeTeleport.add(tpAlea5);
				break;
				
			case 6:
				Teleporteur tpAlea6 = new Teleporteur(new Position(21,7), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][7] = tpAlea6;
	           	listeTeleport.add(tpAlea6);
				break;
				
			case 7:
				Teleporteur tpAlea7 = new Teleporteur(new Position(21,8), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][8] = tpAlea7;
	           	listeTeleport.add(tpAlea7);
				break;
				
			default:
				break;
		}
		
		switch(aleaTelHD)
		{
			case 1:
				Teleporteur tpAlea1 = new Teleporteur(new Position(0,10), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][10] = tpAlea1;
	           	listeTeleport.add(tpAlea1);
				break;
				
			case 2:
				Teleporteur tpAlea2 = new Teleporteur(new Position(0,11), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][11] = tpAlea2;
	           	listeTeleport.add(tpAlea2);
				break;
				
			case 3:
				Teleporteur tpAlea3 = new Teleporteur(new Position(0,12), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][12] = tpAlea3;
	           	listeTeleport.add(tpAlea3);
				break;
			
			case 4:
				Teleporteur tpAlea4 = new Teleporteur(new Position(0,13), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][13] = tpAlea4;
	           	listeTeleport.add(tpAlea4);
				break;
			
			case 5:
				Teleporteur tpAlea5 = new Teleporteur(new Position(0,14), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][14] = tpAlea5;
	           	listeTeleport.add(tpAlea5);
				break;
				
			case 6:
				Teleporteur tpAlea6 = new Teleporteur(new Position(0,15), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][15] = tpAlea6;
	           	listeTeleport.add(tpAlea6);
				break;
				
			case 7:
				Teleporteur tpAlea7 = new Teleporteur(new Position(0,16), Teleporteur.nbCaseTeleportation);
	           	tabCases[0][16] = tpAlea7;
	           	listeTeleport.add(tpAlea7);
				break;
				
			default:
				break;
		}
		
		switch(aleaTelBD)
		{
			case 1:
				Teleporteur tpAlea1 = new Teleporteur(new Position(21,10), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][10] = tpAlea1;
	           	listeTeleport.add(tpAlea1);
				break;
				
			case 2:
				Teleporteur tpAlea2 = new Teleporteur(new Position(21,11), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][11] = tpAlea2;
	           	listeTeleport.add(tpAlea2);
				break;
				
			case 3:
				Teleporteur tpAlea3 = new Teleporteur(new Position(21,12), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][12] = tpAlea3;
	           	listeTeleport.add(tpAlea3);
				break;
			
			case 4:
				Teleporteur tpAlea4 = new Teleporteur(new Position(21,13), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][13] = tpAlea4;
	           	listeTeleport.add(tpAlea4);
				break;
			
			case 5:
				Teleporteur tpAlea5 = new Teleporteur(new Position(21,14), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][14] = tpAlea5;
	           	listeTeleport.add(tpAlea5);
				break;
				
			case 6:
				Teleporteur tpAlea6 = new Teleporteur(new Position(21,15), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][15] = tpAlea6;
	           	listeTeleport.add(tpAlea6);
				break;
				
			case 7:
				Teleporteur tpAlea7 = new Teleporteur(new Position(21,16), Teleporteur.nbCaseTeleportation);
	           	tabCases[21][16] = tpAlea7;
	           	listeTeleport.add(tpAlea7);
				break;
				
			default:
				break;
		}
	}
}
