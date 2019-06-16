package lib;

public class Compteur
{
	private int valeur;
	private int pas;
	private int borneInf;
	private int borneSup;
	
	public Compteur(int borneInf, int borneSup, int pas)
	{
		this.borneInf = borneInf;
		this.valeur = borneInf;
		this.borneSup = borneSup;
		this.pas = pas;
	}
	
	public int getValeur()
	{
		return valeur;
	}
	
	public void incremente()
	{
		valeur = valeur + pas;
		if(valeur > borneSup)
		{
			valeur = borneInf;
		}
	}
	
	public void reinit()
	{
		valeur = borneInf;
	}
	
	public boolean atteintBorneSup()
	{
		return (valeur == borneSup);
	}
}
