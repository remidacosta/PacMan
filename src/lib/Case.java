package lib;

public class Case implements Cloneable
{
	protected Position position;
	
	public Case()
	{
		position = new Position(0,0);
	}
	
	public Case(Position position)
	{
		this.position = position;
	}
	
	 public Object clone()
	  { 
		  Object obj=null;
	    try
	    { 
	      obj=super.clone();
	    } 
	    catch (CloneNotSupportedException x)
	    {
	    	x.printStackTrace(System.err);
	    }
	    return obj;
	}
	 
	public Position getPosition()
	{
		return position;
	}
}
