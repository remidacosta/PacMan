package lib;

public class Position implements Cloneable
{
	private int x;
	private int y;
	
	public Position()
	{
		this(0,0);
	}
	public Position(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x=x;
	}
	
	public void setY(int y)
	{
		this.y=y;
	}
	
	public void setPosition(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void setPosition(Direction d)
	{
		this.x+=d.getX();
		this.y+=d.getY();
	}
	
	 public Position clone()
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
	    return (Position)obj;
	}
	 
	 public boolean equals(Position p)
	 {
		 return (this.x==p.x&&this.y==p.y);
	 }
}
