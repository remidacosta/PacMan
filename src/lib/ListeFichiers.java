package lib;

import java.io.File;
import java.util.ArrayList;

public class ListeFichiers
{
	public static ArrayList<String> getListeNomFichiers(String chemin)
	{
		ArrayList<String> listeFichiers = new ArrayList<String>();
		File repertoire = new File(chemin);
        String liste[] = repertoire.list();      
 
        if (liste != null)
        {         
            for (int i = 0; i < liste.length; i++)
            {
                listeFichiers.add(liste[i]);
            }
        }
        else
        {
            System.err.println("Nom de repertoire invalide");
        }
        
        return listeFichiers;
	}
	
	public static ArrayList<String> getListeNomImages(String chemin)
	{
		ArrayList<String> liste = ListeFichiers.getListeNomFichiers(chemin);
		ArrayList<String> listebis = new ArrayList<String>();
		
		for(int i=0; i<liste.size();i++)
		{
			String [] listesplit = liste.get(i).split("\\.");
			if(listesplit.length >1 && (listesplit[1].equals("png") || listesplit[1].equals("jpg") || listesplit[1].equals("gif")))
			{
				listebis.add(liste.get(i));
			}
		}
		
		return listebis;
	}
	
	public static ArrayList<String> getListeNomImages(String chemin, String extension)
	{
		ArrayList<String> liste = ListeFichiers.getListeNomFichiers(chemin);
		ArrayList<String> listebis = new ArrayList<String>();
		
		for(int i=0; i<liste.size();i++)
		{
			String [] listesplit = liste.get(i).split("\\.");
			if(listesplit.length >1 && (listesplit[1].equals(extension)))
			{
				listebis.add(liste.get(i));
			}
		}
		
		return listebis;
	}
}
