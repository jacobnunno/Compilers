import absyn.*;
import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.Random;


public class SemanticAnalyzer implements SemanticAnalyzerBuilder {

  final static int SPACES = 4;
  //make the hashmap
  HashMap<String, ArrayList<NameDef>> symbolTable = new HashMap<String, ArrayList<NameDef>> ();	
  // record the current function type
  NameTy currentFunctionType = new NameTy( 0, 0, 0);
  
  	

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
		//level++;
		exp.lhs.accept( this, level );
		exp.rhs.accept( this, level );
	}
  }

  public void build( IfExp exp , int level ) {
	if(exp != null)
	{	
		indent( level );
		System.out.println( "Entering a new block: ");
		exp.test.accept( this, level );
		level++;
		exp.thenpart.accept( this, level );
		if (exp.elsepart != null )
		   exp.elsepart.accept( this, level );
    }
    level--;
    indent( level );
    
	deleteScope(level);
    System.out.println( "Leaving the block");
  }

  public void build( IntExp exp , int level ) {
  }

  public void build( OpExp exp , int level ) {
	  if(exp != null)
	{	
		//level++;
		exp.left.accept( this, level );
		exp.right.accept( this, level );
	}
  }

  public void build( VarExp exp , int level ) {
	  if(exp != null)
	{	
		//level++;
		//this will either be an indexVar or a simpleVar
		exp.variable.accept ( this, level);    
	}
  }

  public void build( CallExp exp , int level ) {
	  //check if call expression has a function in hashmap
	  
	  boolean foundFlag = false;
	  
	  for (ArrayList list : symbolTable.values()) {
		  if(list.isEmpty() == false)
		  {
			  for (int i = 0; i < list.size(); i++) 
			  {
				  NameDef current = (NameDef)list.get(i);
				  String currentName = current.name;
				  if (exp.func.equals(currentName) && current.level == 0)
				  {
					  foundFlag = true;
					  //if we want to add checking the amount of params vs arguments
					  /*
					  FunctionDec fDec = (FunctionDec)(current.dec);
					  if(exp.args.size() == fDec.params.size())
					  {
						System.err.println("correct amount of arguments");
					  }
					  */
				  }		
			  }
		  }
	  }
	  if(!foundFlag)
	  {
		  int row = exp.row;
          int col = exp.col;
          System.err.println("Undeclared Call: " + exp.func  + " Row " + row + " Col " + col);
	  } 
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
	 //level++;
		//this goes to SimpleVariable
		iVar.index.accept( this, level);
	}
  }

  public void build( SimpleVar sVar , int level ) {
	if(sVar != null)
	{	
		NameDef nDef = new NameDef(sVar.name, level, null);
		if(isDuplicateInScope(nDef) == false)
		{
			int row = sVar.row;
            int col = sVar.col;
            System.err.println("Undeclared Variable: " + sVar.name  + " Row " + row + " Col " + col);
		}
	}
  }
  
  public void build( ReturnExp rExp , int level ) {
	  //check if rExp has same type as currentFunctionType

  }
  
  public void build( NilExp nExp , int level ) {
  }

  public void build( WhileExp exp , int level ) {
	  if(exp != null)
	{	
		indent( level );		
		System.out.println( "Entering a new block: ");
		exp.test.accept( this, level);
	
		level++;
		exp.block.accept( this, level);		
		level--;
		indent( level );
		
		deleteScope(level);
		System.out.println( "Leaving the block");
	}
  }
  
  public void build( ArrayDec exp , int level ) {
	if(exp != null)
	{	
		indent( level );
		NameDef arrayDef = new NameDef(exp.name,level,exp);
		
		//check for duplicate array declaration	
		if (isDuplicateInScope(arrayDef) == false){	
			addHash(arrayDef);	
		}	
		else {	
			int row = arrayDef.dec.row;	
			int col = arrayDef.dec.col;	
			System.err.println("Duplicate Array Declaration: " + arrayDef + " Row " + row + " Col " + col);	
		}	
		
		addHash(arrayDef);
		System.out.print(exp.name + ": ");
		build(exp.typ, level);
	}
  }
  
  public void build( FunctionDec exp , int level ) {
	if(exp != null)
	{	
		//add as current function type for return statement
		currentFunctionType = exp.result;
		indent( level );
		System.out.println( "Entering the scope for function "  + exp.func);
		
		NameDef funcDef = new NameDef(exp.func,level,exp);
		
		//check for duplicate
		if (isDuplicateInScope(funcDef) == false){
			addHash(funcDef);
		}
		else {
			int row = funcDef.dec.row;
			int col = funcDef.dec.col;
			System.err.println("Duplicate Function Declaration: " + funcDef  + " Row " + row + " Col " + col);
			addHash(funcDef);
		}
		
		level++;
		exp.params.accept(this, level);
		exp.body.accept(this, level);
		level--;
		indent( level );	
		deleteScope(level);
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
		
		//check for duplicate simple declaration
		if (isDuplicateInScope(simpleDef) == false){
			addHash(simpleDef);
		}
		else {
			int row = simpleDef.dec.row;
			int col = simpleDef.dec.col;
			System.err.println("Duplicate Simple Variable Declaration: " + simpleDef + " Row " + row + " Col " + col);
		}
		
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
            System.out.print((arrli.get(i)).toString() + "\n");  
  }
  
  public void printHash()
  {
	  System.out.println( "************************ printing Hash\n");
	  for (ArrayList list : symbolTable.values()) {
		  printList(list);
	  }
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
  
  // check for a duplicate definition in the same scope
  // @param NameDef newOne - the new definition we want to add to the symbol table
  // @return boolean - true if there is a duplicate false if there is no duplicate
  public boolean isDuplicateInScope(NameDef newOne){
	  for (ArrayList list : symbolTable.values()) {
		  if(list.isEmpty() == false)
		  {
			  //check first entry of each key value pair
			  NameDef head = (NameDef)list.get(0);
			  String currentName = head.name;
			  int currentLevel = head.level;
			  
			   //System.err.println("****************************" + newOne.name + " = " + currentName + " AND " + newOne.level + " = " + currentLevel);
			  
			  //check name of definition and check scope of definiton
			  if (newOne.name.equals(currentName) && newOne.level == currentLevel)
			  {
				  return true;
			  }		  
		  }
	  }
	  return false;
  }
  
  public void deleteScope(int level)
  {
	int levelCheck = level + 1;
	for (ArrayList list : symbolTable.values()) {
		  
		  //check first entry of each key value pair
		  if(list.isEmpty() == false)
		  {
			  NameDef head = (NameDef)list.get(0);
			  int currentLevel = head.level;
			  
			  //check name of definition and check scope of definiton
			  if (levelCheck == currentLevel)
			  {
				  list.remove(0);
			  }
		  }
	  }
  }
/*  
  public Exp checkType(Exp exp)
  {
	  if(exp instanceOf VarExp)
	  {
				if()
	  }
	  
	  else
	  {
		  if(exp instanceOf OpExp)
		  {
				checkType(exp.lhs);
				checkType(exp.rhs);
		  }
		  else if(exp instanceOf VarExp)
		  {
				checkType(exp.variable);
		  }
	  }
	  
  }*/
  
}
