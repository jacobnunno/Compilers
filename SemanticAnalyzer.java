import absyn.*;
import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.Random;


public class SemanticAnalyzer implements SemanticAnalyzerBuilder {

  final static int SPACES = 4;
  //make the hashmap
  HashMap<String, ArrayList<NameDef>> symbolTable = new HashMap<String, ArrayList<NameDef>> ();	
  	

  private void indent( int level, int scope ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
    scope++;
  }

  public void build( ExpList expList , int level, int scope ) {
    while( expList != null && expList.head != null) {
      expList.head.accept( this, level, scope );
      expList = expList.tail;
    } 
  }
  
  public void build( DecList decList , int level, int scope ) {
    while( decList != null ) {
	  System.out.println( "Entering the Global level:" );
      decList.head.accept( this, level, scope );
      decList = decList.tail;
    } 
  }
  
  public void build( VarDecList varDecList , int level, int scope ) {
    while( varDecList != null && varDecList.head != null) {
      varDecList.head.accept( this, level, scope );
      varDecList = varDecList.tail;
    } 
  }

  public void build( AssignExp exp , int level, int scope ) {
	if(exp != null)
	{	
		indent( level, scope );
		System.out.println( "AssignExp:" );
	 level++;
		exp.lhs.accept( this, level, scope );
		exp.rhs.accept( this, level, scope );
	}
  }

  public void build( IfExp exp , int level, int scope ) {
	if(exp != null)
	{	
    indent( level, scope );
    System.out.println( "IfExp:" );
    level++;
    exp.test.accept( this, level, scope );
    exp.thenpart.accept( this, level, scope );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, level, scope );
   }
  }

  public void build( IntExp exp , int level, int scope ) {
	if(exp != null)
	{	
		indent( level, scope );
		System.out.println( "IntExp: " + exp.value ); 
	}
  }

  public void build( OpExp exp , int level, int scope ) {
	  if(exp != null)
	{	
		indent( level, scope );
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
		exp.left.accept( this, level, scope );
		exp.right.accept( this, level, scope );
	}
  }

  public void build( VarExp exp , int level, int scope ) {
	  if(exp != null)
	{	
		indent( level, scope );
		System.out.println( "VarExp: ");
	 level++;
		//this will either be an indexVar or a simpleVar
		exp.variable.accept ( this, level, scope);    
	}
  }

  public void build( CallExp exp , int level, int scope ) {
	  if(exp != null)
	{	
		indent( level, scope );
		System.out.println( "CallExp: " + exp.func );
	 level++;
		build(exp.args, level, scope);
	}
  }
  
  public void build( CompoundExp exp , int level, int scope ) {
	 if(exp != null)
	{	
		indent( level, scope );
		System.out.println( "CompoundExp: ");
	 level++;
		build(exp.varDecList, level, scope);
		build(exp.expList, level, scope);
	}
  }
  
  public void build( IndexVar iVar , int level, int scope ) {
	  if(iVar != null)
	{	
		indent( level, scope );
		System.out.println( "IndexVar: " + iVar.name );
	 level++;
		//this goes to SimpleVariable
		iVar.index.accept( this, level, scope);
	}
  }

  public void build( SimpleVar sVar , int level, int scope ) {
	  if(sVar != null)
	{	
		indent( level, scope );
		System.out.println( "SimpleVar: " + sVar.name );
	}
  }
  
  public void build( ReturnExp rExp , int level, int scope ) {
	  if(rExp != null)
	{	
		indent( level, scope );
		System.out.println( "ReturnExp: ");
	 level++;
		rExp.exp.accept( this, level, scope);
	}
  }
  
  public void build( NilExp nExp , int level, int scope ) {
	  if(nExp != null)
	{	
		indent( level, scope );
		System.out.println( "NilExp: ");
	}
  }

  public void build( WhileExp exp , int level, int scope ) {
	  if(exp != null)
	{	
		indent( level, scope );
		System.out.println( "WhileExp: " );
	 level++;
		exp.test.accept( this, level, scope);
		exp.block.accept( this, level, scope);
	}
  }
  
  public void build( ArrayDec exp , int level, int scope ) {
	  if(exp != null)
	{	
		indent( level, scope );
		NameDef simpleDef = new NameDef();
		simpleDef.name =  exp.name;
		simpleDef.scope = scope;
		simpleDef.dec = exp;
		
		addHash(simpleDef);
		build(exp.typ, level, scope);
	}
  }
  
  public void build( FunctionDec exp , int level, int scope ) {
	if(exp != null)
	{	
		indent( level, scope );
		//TODO get rid of these stars
		System.out.print( "*****************************\n");
		System.out.print( "Entering the scope for function "  + exp.func);
		
		NameDef funcDef = new NameDef();
		funcDef.name =  exp.func;
		funcDef.scope = scope;
		funcDef.dec = exp;
		
		addHash(funcDef);
		
		exp.result.accept( this, level, scope);
		level++;
		exp.params.accept(this, level, scope);
		exp.body.accept(this, level, scope);
	}
  }
  
  public void build( NameTy exp , int level, int scope ) {
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
  
  public void build( SimpleDec exp , int level, int scope ) {
	if(exp != null)
	{	
		indent( level, scope );
		
		NameDef simpleDef = new NameDef();
		simpleDef.name =  exp.name;
		simpleDef.scope = scope;
		simpleDef.dec = exp;
		
		addHash(simpleDef);
		
		
		build(exp.typ, level, scope);
	}
  }
  
  public void build( ErrorExp err , int level, int scope ) {
	  if(err != null)
	{	
		indent( level, scope );
		System.out.println( "Expression Error on line: " + err.row + " Column: " + err.col);
	}
  }
  
  public void build( ErrorDec err , int level, int scope ) {
	  if(err != null)
	{	
		indent( level, scope );
		System.out.println( "Declaration Error on line: " + err.row + " Column: " + err.col);
	}
  }
  
  public void build( ErrorVarDec err , int level, int scope ) {
	  if(err != null)
	{	
		indent( level, scope );
		System.out.println( "Variable Declaration Error on line: " + err.row + " Column: " + err.col);
	}
  }
  
  public void printList(ArrayList arrli)
  {
	 for (int i=0; i<arrli.size(); i++) 
            System.out.print(arrli.get(i)+" ");  
  }
    
  public String getKey()
  {
	  Random rand = new Random();
	  int n = rand.nextInt(10000);
	  return String.valueOf(n);
  }

  public void addHash(NameDef nDef)
  {
	String key = getKey();
	boolean keyFound = false;
	
	for (String i : symbolTable.keySet()) 
	{
		//collision so we add to existing arraylist
	  if(i.equals(key))
	  {
		  (symbolTable.get(i)).add(0, nDef);
		  keyFound = true;
		  printList(symbolTable.get(i));
		  System.out.println( " Scope:  ");
	  }
	}
	if(!keyFound)
	{
		//make new list and add
		ArrayList<NameDef> arrayList = new ArrayList<NameDef>();
		arrayList.add(0, nDef);
	
		symbolTable.put(key, arrayList);
		printList(arrayList);
	}  
  }
}

