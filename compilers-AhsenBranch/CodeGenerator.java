import absyn.*;
import java.io.*;

public class CodeGenerator implements AbsynVisitor {

  final static int SPACES = 4;

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }

  public void visit( ExpList expList, int level ) {
    while( expList != null && expList.head != null) {
      expList.head.accept( this, level );
      expList = expList.tail;
    } 
  }
  
   public void visit( DecList decList, int level ) {
    while( decList != null ) {
      decList.head.accept( this, level );
      decList = decList.tail;
    } 
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

}
