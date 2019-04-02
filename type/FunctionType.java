package type;

import java.util.*;

public class FunctionType extends Type  {
	public int type;
	public ArrayList<Type> params;
	
	public FunctionDec functionPointer;
	
	public FunctionType(int type, ArrayList<Type> params, FunctionDec functionPointer) 
	{
			this.type = type;
			this.params = params;	
			this.functionPointer = functionPointer;
	}
}
