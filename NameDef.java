import absyn.*;

public class NameDef{
	public String name; 
	public int level; 
	public Dec dec;
	
	public NameDef(String name, int level, Dec dec)
	{
		this.name = name;
		this.level = level;
		this.dec = dec;
	}
	
	public String toString(){
		return ("name is " + this.name + " level is " + this.level);
	}
}
