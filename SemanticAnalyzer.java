import absyn.*;
import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.Random;


public class SemanticAnalyzer implements SemanticAnalyzerBuilder {

  final static int SPACES = 4;
  //make the hashmap
  HashMap<String, ArrayList<NameDef>> symbolTable = new HashMap<String, ArrayList<NameDef>> ();	
  	

  // check for a duplicate definition in the same scope
  // @param NameDef newOne - the new definition we want to add to the symbol table
  // @return boolean - true if there is a duplicate false if there is no duplicate
  public boolean isDuplicateInScope(NameDef newOne){
	  for (ArrayList list : symbolTable.values()) {
		  
		  //check first entry of each key value pair
		  NameDef head = (NameDef)list.get(0);
		  String currentName = head.name;
		  int currentLevel = head.level;
		  
		  //check name of definition and check scope of definiton
		  if (newOne.name.equals(currentName) && newOne.level == currentLevel)
		  {
			  return true;
		  }
	  }
	  return false;
  }

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) 
		System.out.print( " " );
  }

  public void build( ExpList expList , int level ) {
    while( expList != null && expList.head != null) {
      expList.head.accept( this, level );
      expList = expList.tail;
    } 
  }
  
  public void build( DecList decList , int level ) {	  
	System.out.println( "Entering the Global level:" );
    while( decList != null ) {
      decList.head.accept( this, level );
      decList = decList.tail;
    } 
    System.out.println( "Leaving the Global level" );
  }
  
  public void build( VarDecList varDecList , int level ) {
    while( varDecList != null && varDecList.head != null) {
      varDecList.head.accept( this, level );
      varDecList = varDecList.tail;
    } 
  }

  public void build( AssignExp exp , int level ) {
	if(exp != null)
	{	
		level++;
		exp.lhs.accept( this, level );
		exp.rhs.accept( this, level );
	}
  }

  public void build( IfExp exp , int level ) {
	if(exp != null)
	{	
		indent( level );
		System.out.println( "Entering a new block: ");
		level++;
		exp.test.accept( this, level );
		exp.thenpart.accept( this, level );
		if (exp.elsepart != null )
		   exp.elsepart.accept( this, level );
    }
    level--;
    indent( level );
    System.out.println( "Leaving the block");
  }

  public void build( IntExp exp , int level ) {
  }

  public void build( OpExp exp , int level ) {
	  if(exp != null)
	{	
		level++;
		exp.left.accept( this, level );
		exp.right.accept( this, level );
	}
  }

  public void build( VarExp exp , int level ) {
	  if(exp != null)
	{	
		level++;
		//this will either be an indexVar or a simpleVar
		exp.variable.accept ( this, level);    
	}
  }

  public void build( CallExp exp , int level ) {
  }
  
  public void build( CompoundExp exp , int level ) {
	 if(exp != null)
	{			
		build(exp.varDecList, level);
		build(exp.expList, level);
	}
  }
  
  public void build( IndexVar iVar , int level ) {
	  if(iVar != null)
	{	
	 level++;
		//this goes to SimpleVariable
		iVar.index.accept( this, level);
	}
  }

  public void build( SimpleVar sVar , int level ) {
	if(sVar != null)
	{	
	}
  }
  
  public void build( ReturnExp rExp , int level ) {
  }
  
  public void build( NilExp nExp , int level ) {
  }

  public void build( WhileExp exp , int level ) {
	  if(exp != null)
	{	
		indent( level );		
		System.out.println( "Entering a new block: ");
		level++;
		exp.test.accept( this, level);
		exp.block.accept( this, level);		
		level--;
		indent( level );
		System.out.println( "Leaving the block");
	}
  }
  
  public void build( ArrayDec exp , int level ) {
	  if(exp != null)
	{	
		indent( level );
		NameDef arrayDef = new NameDef(exp.name,level,exp);
		
		
		addHash(arrayDef);
		System.out.print(exp.name + ": ");
		build(exp.typ, level);
	}
  }
  
  public void build( FunctionDec exp , int level ) {
	if(exp != null)
	{	
		indent( level );
		System.out.println( "Entering the scope for function "  + exp.func);
		
		NameDef funcDef = new NameDef(exp.func,level,exp);
		
		//check for duplicate
		if (isDuplicateInScope(funcDef) == false){
			addHash(funcDef);
		}
		else {
			System.err.println("Found Duplicate Function Declaration");
			addHash(funcDef);
		}
		
		level++;
		exp.params.accept(this, level);
		exp.body.accept(this, level);
		level--;
		indent( level );		
		System.out.println( "Leaving the scope for function "  + exp.func);
	}
  }
  
  public void build( NameTy exp , int level ) {
	if(exp != null)
	{	
		if(exp.typ == NameTy.VOID)
		{
			System.out.println( "VOID");		
		}
		else
		{
			System.out.println( "INT");		
		}
	}
  }
  
  public void build( SimpleDec exp , int level ) {
	if(exp != null)
	{			
		NameDef simpleDef = new NameDef(exp.name,level,exp);
		
		addHash(simpleDef);
		indent( level );
		System.out.print(exp.name + ": ");
		exp.typ.accept( this, level);
		//build(exp.typ, level);
	}
  }
  
  public void build( ErrorExp err , int level ) {
	  if(err != null)
	{	
		indent( level );
		System.out.println( "Expression Error on line: " + err.row + " Column: " + err.col);
	}
  }
  
  public void build( ErrorDec err , int level ) {
	  if(err != null)
	{	
		indent( level );
		System.out.println( "Declaration Error on line: " + err.row + " Column: " + err.col);
	}
  }
  
  public void build( ErrorVarDec err , int level ) {
	  if(err != null)
	{	
		indent( level );
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
		  //printList(symbolTable.get(i));
		  //System.out.print( " Scope:  " + nDef.level + " ");
	  }
	}
	if(!keyFound)
	{
		//make new list and add
		ArrayList<NameDef> arrayList = new ArrayList<NameDef>();
		arrayList.add(0, nDef);
	
		symbolTable.put(key, arrayList);
		//printList(arrayList);
		//System.out.print( " Scope:  " + nDef.level + " ");
	}  
  }
}

