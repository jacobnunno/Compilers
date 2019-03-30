import absyn.*;
import java.io.*;

public class ShowTreeVisitor implements AbsynVisitor {

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
		indent( level );
		System.out.println( "AssignExp:" );
		level++;
		exp.lhs.accept( this, level );
		exp.rhs.accept( this, level );
	}
  }

  public void visit( IfExp exp, int level ) {
	if(exp != null)
	{	
    indent( level );
    System.out.println( "IfExp:" );
    level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, level );
   }
  }

  public void visit( IntExp exp, int level ) {
	if(exp != null)
	{	
		indent( level );
		System.out.println( "IntExp: " + exp.value ); 
	}
  }

  public void visit( OpExp exp, int level ) {
	  if(exp != null)
	{	
		indent( level );
		System.out.print( "OpExp:" ); 
		switch( exp.op ) {
		  case OpExp.PLUS:
			System.out.println( " + " );
			break;
		  case OpExp.MINUS:
			System.out.println( " - " );
			break;
		  case OpExp.MUL:
			System.out.println( " * " );
			break;
		  case OpExp.DIV:
			System.out.println( " / " );
			break;
		  case OpExp.EQ:
			System.out.println( " = " );
			break;
		  case OpExp.NE:
			System.out.println( " != " );
			break;
		  case OpExp.LT:
			System.out.println( " < " );
			break;
		  case OpExp.LE:
			System.out.println( " <= " );
			break;
		  case OpExp.GT:
			System.out.println( " > " );
			break;
		  case OpExp.GE:
			System.out.println( " >= " );
			break;
		  default:
			System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
		}
		level++;
		exp.left.accept( this, level );
		exp.right.accept( this, level );
	}
  }

  public void visit( VarExp exp, int level ) {
	  if(exp != null)
	{	
		indent( level );
		System.out.println( "VarExp: ");
		level++;
		//this will either be an indexVar or a simpleVar
		exp.variable.accept ( this, level);    
	}
  }

  public void visit( CallExp exp, int level ) {
	  if(exp != null)
	{	
		indent( level );
		System.out.println( "CallExp: " + exp.func );
		level++;
		visit(exp.args, level);
	}
  }
  
  public void visit( CompoundExp exp, int level ) {
	 if(exp != null)
	{	
		indent( level );
		System.out.println( "CompoundExp: ");
		level++;
		visit(exp.varDecList, level);
		visit(exp.expList, level);
	}
  }
  
  public void visit( IndexVar iVar, int level ) {
	  if(iVar != null)
	{	
		indent( level );
		System.out.println( "IndexVar: " + iVar.name );
		level++;
		//this goes to SimpleVariable
		iVar.index.accept( this, level);
	}
  }

  public void visit( SimpleVar sVar, int level ) {
	  if(sVar != null)
	{	
		indent( level );
		System.out.println( "SimpleVar: " + sVar.name );
	}
  }
  
  public void visit( ReturnExp rExp, int level ) {
	  if(rExp != null)
	{	
		indent( level );
		System.out.println( "ReturnExp: ");
		level++;
		rExp.exp.accept( this, level);
	}
  }
  
  public void visit( NilExp nExp, int level ) {
	  if(nExp != null)
	{	
		indent( level );
		System.out.println( "NilExp: ");
	}
  }

  public void visit( WhileExp exp, int level ) {
	  if(exp != null)
	{	
		indent( level );
		System.out.println( "WhileExp: " );
		level++;
		exp.test.accept( this, level);
		exp.block.accept( this, level);
	}
  }
  
  public void visit( ArrayDec exp, int level ) {
	  if(exp != null)
	{	
		indent( level );
		System.out.print( "ArrayDec: "  + exp.name + " [" + exp.size.value + "] ");
		//exp.size.accept( this, level);
		visit(exp.typ, level);
	}
  }
  
  public void visit( FunctionDec exp, int level ) {
	  if(exp != null)
	{	
		indent( level );
		System.out.print( "FunctionDec: "  + exp.func + " ");
		exp.result.accept( this, level);
		level++;
		exp.params.accept(this, level);
		exp.body.accept(this, level);
	}
  }
  
  public void visit( NameTy exp, int level ) {
	if(exp != null)
	{	
		if(exp.typ == NameTy.VOID)
		{
			System.out.println( "- VOID");		
		}
		else
		{
			System.out.println( "- INT");		
		}
	}
  }
  
  public void visit( SimpleDec exp, int level ) {
	  if(exp != null)
	{	
		indent( level );
		System.out.print( "SimpleDec: "  + exp.name + " ");
		visit(exp.typ, level);
	}
  }
  
  public void visit( ErrorExp err, int level ) {
	  if(err != null)
	{	
		indent( level );
		System.out.println( "Expression Error on line: " + err.row + " Column: " + err.col);
	}
  }
  
  public void visit( ErrorDec err, int level ) {
	  if(err != null)
	{	
		indent( level );
		System.out.println( "Declaration Error on line: " + err.row + " Column: " + err.col);
	}
  }
  
  public void visit( ErrorVarDec err, int level ) {
	  if(err != null)
	{	
		indent( level );
		System.out.println( "Variable Declaration Error on line: " + err.row + " Column: " + err.col);
	}
  }

}
