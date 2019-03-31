import absyn.*;
import java.io.*;

public class CodeGenerator implements AbsynVisitor {

  final static int SPACES = 4;
  static int nameCtr = 0;
  
  public int iOffset = 0;
  public int globalOffset = 0;
  
  //line counter
  public int emitLoc = 0;
  //points to the top of the frame
  public int highEmitLoc = 0;
  //not sure if it should init as true or false
  public boolean TraceCode = false;

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }
  
  private String newTemp()
  {
	  nameCtr++;
	  return "t" + Integer.toString(nameCtr);  
  }

  public void visit( ExpList expList, int level ) {
    while( expList != null && expList.head != null) {
      expList.head.accept( this, level );
      expList = expList.tail;
    } 
  }
  
public void visit( DecList decList, int level ) {
	System.out.println("* Standard prelude");
	//prelude

	System.out.println("0:	LD  6,0(0)");
	System.out.println("1:	LDA  5,0(6)");
	System.out.println("2:	ST  0,0(0)");
	System.out.println("4:	ST  0,-1(5)");
	System.out.println("5:	IN  0,0,0");
	System.out.println("6:	LD  7,-1(5)");
	System.out.println("7:	ST  0,-1(5)");
	System.out.println("8:	LD  0,-2(5)");
	System. out.println("9:	OUT  0,0,0");
	System. out.println("10:	LD  7,-1(5)");
	System.out.println("3:	LDA  7,7(7)");
      
      
    //generating code  
      
    while( decList != null ) {
      decList.head.accept( this, level );
      decList = decList.tail;
    } 
    
    
    
    //end of execution
    System.out.println("end of execution");
    System.out.println("HALT  0,0,0");
  }
  
  public void visit( VarDecList varDecList, int level ) {
    while( varDecList != null && varDecList.head != null) {
      varDecList.head.accept( this, level );
      varDecList = varDecList.tail;
    } 
  }

  public void visit( AssignExp exp, int level ) {
	if(exp != null)
	{	

		//level++;
		exp.lhs.accept( this, level );
		exp.rhs.accept( this, level );
	}
  }

  public void visit( IfExp exp, int level ) {
	if(exp != null)
	{	

    //level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, level );
   }
  }

  public void visit( IntExp exp, int level ) {
	if(exp != null)
	{	

	}
  }

  public void visit( OpExp exp, int level ) {
	  if(exp != null)
	{	
		//level++;
		exp.left.accept( this, level );
		exp.right.accept( this, level );
	}
  }

  public void visit( VarExp exp, int level ) {
	  if(exp != null)
	{	
		//level++;
		//this will either be an indexVar or a simpleVar
		exp.variable.accept ( this, level);    
	}
  }

  public void visit( CallExp exp, int level ) {
	  if(exp != null)
	{	
		//level++;
		visit(exp.args, level);
	}
  }
  
  public void visit( CompoundExp exp, int level ) {
	 if(exp != null)
	{	
		//level++;
		visit(exp.varDecList, level);
		visit(exp.expList, level);
	}
  }
  
  public void visit( IndexVar iVar, int level ) {
	  if(iVar != null)
	{	
		//level++;
		//this goes to SimpleVariable
		iVar.index.accept( this, level);
	}
  }

  public void visit( SimpleVar sVar, int level ) {
	  if(sVar != null)
	{	

	}
  }
  
  public void visit( ReturnExp rExp, int level ) {
	  if(rExp != null)
	{	
		//level++;
		rExp.exp.accept( this, level);
	}
  }
  
  public void visit( NilExp nExp, int level ) {
	  if(nExp != null)
	{	
		
	}
  }

  public void visit( WhileExp exp, int level ) {
	  if(exp != null)
	{	
		//level++;
		exp.test.accept( this, level);
		exp.block.accept( this, level);
	}
  }
  
  public void visit( ArrayDec exp, int level ) {
	  if(exp != null)
	{	
		//exp.size.accept( this, level);
		visit(exp.typ, level);
	}
  }
  
  public void visit( FunctionDec exp, int level ) {
	  if(exp != null)
	{	
		exp.result.accept( this, level);
		//level++;
		exp.params.accept(this, level);
		exp.body.accept(this, level);
	}
  }
  
  public void visit( NameTy exp, int level ) {
	if(exp != null)
	{	
		
	}
  }
  
  public void visit( SimpleDec exp, int level ) {
	  if(exp != null)
	{	
		visit(exp.typ, level);
	}
  }
  
  public void visit( ErrorExp err, int level ) {
	  if(err != null)
	{	
	}
  }
  
  public void visit( ErrorDec err, int level ) {
	  if(err != null)
	{	
		
	}
  }
  
  public void visit( ErrorVarDec err, int level ) {
	  if(err != null)
	{	
		
	}
  }

	public void emitComment(String comment)
	{
			 System.out.println("* " + comment);
	}
	
	public int emitSkip( int distance ) 
	{
		int i= emitLoc;
		emitLoc += distance;
		if( highEmitLoc < emitLoc) 
			highEmitLoc= emitLoc;
		return i;
	}
	
	void emitBackup( int loc) 
	{
		if(loc > highEmitLoc)
		{
			emitComment("BUG in emitBackup"); 
		}
		emitLoc = loc;
	} 
	
	void emitRestore() 
	{
		emitLoc= highEmitLoc;
	}
	
	void emitRM_Abs( String op, int r, int a, String c ) 
	{
		System.out.println(emitLoc + ":		" + op +  " " + r + ", " + (a - (emitLoc + 1)) + "(" + r + ")");
		emitLoc++;
		if( TraceCode) 
			System.out.println("	" + c );
		if( highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;
	}
	
	void emitRM( ) 
	{

	}
	
	void emitRO(  ) 
	{

	}
}
