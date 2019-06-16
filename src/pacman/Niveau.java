package pacman;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import lib.Entite;

public class Niveau
{
	private int numNiveau;
	
	Niveau(ArrayList<Entite> listeEntite, TerrainPacman terrain)
	{
		this(1,listeEntite,terrain);
	}
	
	Niveau(int num, ArrayList<Entite> listeEntite, TerrainPacman terrain)
	{
		numNiveau = num;
		supprimerFantomesExistants(listeEntite);	
		remplirListeDeFantomes(listeEntite, terrain);
	}
	
	public int getNumNiveau()
	{
		return numNiveau;
	}
	
	private void supprimerFantomesExistants(ArrayList<Entite> listeEntite)
	{
		for(int i=listeEntite.size()-1; i>=0;i--)
		{
			if(listeEntite.get(i) instanceof Fantome)
			{
				listeEntite.remove(i);
			}
		}
	}
	
	private void remplirListeDeFantomes(ArrayList<Entite> listeEntite, TerrainPacman terrain)
	{	
		try
		{
			InputStream flux=new FileInputStream("data/niveaux.txt"); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			while ((ligne=buff.readLine())!=null)
			{
				if(ligne.equals(""+numNiveau))
				{
					ligne=buff.readLine();
					for(int i=0; i<ligne.length();i++)
					{
						if(ligne.charAt(i) == 'C')
						{
							FantomeBleu fb = new FantomeBleu(terrain);
							listeEntite.add(fb);
						}
						else if(ligne.charAt(i) == 'T')
						{
							FantomeRouge fr = new FantomeRouge(terrain);
							listeEntite.add(fr);
						}
						else if(ligne.charAt(i) == 'F')
						{
							/*FantomeVert fv = new FantomeVert();
							listeFantomes.add(fv);*/
						}
					}
				}
			}
			buff.close(); 
		}		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
