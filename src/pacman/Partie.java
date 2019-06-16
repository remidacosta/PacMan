package pacman;

import java.util.ArrayList;
import java.util.Observable;

import lib.Direction;
import lib.Entite;
import lib.ListeFichiers;
import lib.Position;

public class Partie extends Observable implements Runnable
{
	private TerrainPacman terrain;
	private Niveau niveau;
	private ChoixTerrain choix;
	private boolean stop;
	private boolean pause;
	
	public Partie(ChoixTerrain choix)
	{	
		this.choix = choix;
		initTerrain();
		
		ArrayList<Entite> listeEntite = new ArrayList<Entite>();
		for(int i=0; i<terrain.getNbJoueurs();i++)
		{
			listeEntite.add(new Pacman(terrain));
		}
		
		niveau=new Niveau(2,listeEntite,terrain);
		terrain.setEntites(listeEntite);
		stop = false;
	}
	
	public Partie(String chemin)
	{
		this.choix = ChoixTerrain.sauvegarde;
		terrain = new TerrainPacman(chemin);
		
		ArrayList<Entite> listeEntite = new ArrayList<Entite>();
		for(int i=0; i<terrain.getNbJoueurs();i++)
		{
			listeEntite.add(new Pacman(terrain));
		}
		
		niveau=new Niveau(2,listeEntite,terrain);
		terrain.setEntites(listeEntite);
		stop=false;
	}
	
	private void initTerrain()
	{
		switch(choix)
		{
			case sauvegarde:
				terrain = new TerrainPacman(terrain.getChemin());
				break;
				
			case classique1joueur:
				terrain = new TerrainPacman(1);
				break;
			
			case classique2joueurs:
				terrain = new TerrainPacman(2);
				break;
				
			case aleatoire1joueur:
				terrain = new TerrainAleat(1);
				break;
			
			case aleatoire2joueurs:
				terrain = new TerrainAleat(2);
				break;

			//cas à rajouter + autre constructeur pour le choix du terrain editable OU rajouter parametre ici
			default:
				terrain = new TerrainPacman();
				break;
		}
	}
	
	public void niveauSuivant()
	{
		ArrayList<Entite> listEntite = terrain.getEntites();
		initTerrain();
		int numNiveautmp = niveau.getNumNiveau() + 1;
		
		do
		{
			niveau=new Niveau(numNiveautmp,listEntite,terrain);
			numNiveautmp = 1;
		}while(listEntite.size() <= 1);
		
		terrain.setEntites(listEntite);
		terrain.getEntites().get(0).setPosPerso(new Position(16,9));
		((Pacman)terrain.getEntites().get(0)).setTerrain(terrain);
	}
	
	public boolean partiePerdue()
	{
		return(((Perso)terrain.getEntites().get(0)).getVie() == 0);
	}
	
	public boolean partieGagnee()
	{
		return(terrain.getNbPastillesRest() == 0);
	}
		
	public TerrainPacman getTerrain()
	{
		return terrain;
	}
	
	public ChoixTerrain getChoixTerrain()
	{
		return choix;
	}
	
	public void ActionPacman(Direction suivante)
	{	
		((Pacman)(terrain.getEntites().get(0))).setProchaineDirection(suivante);
	}
	
	public void ActionPouvoir()
	{	
		if(((Pacman)(terrain.getEntites().get(0))).getPouvoir()!=null)
		{
			if(((Pacman)(terrain.getEntites().get(0))).pouvoirEstActif())
			{
				((Pacman)(terrain.getEntites().get(0))).desactiverPouvoir();
			}
			else
			{
				((Pacman)(terrain.getEntites().get(0))).activerPouvoir();
			}
			
		}
	}
	
	public void run()
	{
		while(!stop)
		{
			while(!pause)
			{
				for(Entite p: terrain.getEntites())
				{
					p.Deplacer();
				}
				setChanged();
				notifyObservers();
				try
				{
					Thread.sleep(200);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stop()
	{
		pause=true;
		stop =true;
	}
	
	public void pause()
	{
		pause =!pause;
	}
	
	public boolean getPause()
	{
		return pause;
	}
}
