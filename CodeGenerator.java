import absyn.*;
import java.io.*;

public class CodeGenerator implements AbsynVisitor {

  final static int SPACES = 4;

  /***************	iMem offsets	*******************/
  //line counter
  public int emitLoc = 0;
  //points to the top of the frame
  public int highEmitLoc = 0;
  
   /***************	dMem offsets	*******************/
  //ths is GP
  public int globalOffset = 0;
  //this is FP
  public int frameOffset = -1;
  
  //not sure if it should init as true or false
  public boolean TraceCode = false;
  public int entry = 0;
  
  private void loadCode(Exp exp)
  {
	  if(exp instanceof VarExp)
	  {
		  //check offset
		  
	  }
	  else if(exp instanceof AssignExp)
	  {
		  //might not be LDA
			//emitRM("LDA", 0, 0,0, "load assign value" );  
	  }
	  else if(exp instanceof IntExp)
	  {
	  }
	  else if(exp instanceof CallExp)
	  {
		  //might not be LDA
			//emitCode("LOADING OP", 0, 0,0, "loading callExp" );
	  }
  }
  
  private int newframeOffset()
  {
	  frameOffset--;
	  //System.out.println("***** frame offset" + frameOffset);
	  return frameOffset;  
  }

  public void visit( ExpList expList, int level ) {
    while( expList != null && expList.head != null) {
      expList.head.accept( this, level-- );
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
	emitLoc = 11;
      
      
    //generating code  
      
    while( decList != null ) {
      decList.head.accept( this, level );
      decList = decList.tail;
    } 
    
    //end of execution
    emitRM( "ST", 5, globalOffset+0, 5, "push ofp" );
    emitRM( "LDA", 5, globalOffset, 5, "push frame" );
    emitRM( "LDA", 0, 1, 7, "load ac with ret ptr" );
    emitRM_Abs( "LDA", 7, entry, "jump to main loc" );
    emitRM( "LD", 5, 0, 5, "pop frame" );
    emitRO( "HALT", 0, 0, 0, "" );
  }
  
  public void visit( VarDecList varDecList, int level ) {
    while( varDecList != null && varDecList.head != null) {
      varDecList.head.accept( this, level--);
      varDecList = varDecList.tail;
    } 
  }

  public void visit( AssignExp exp, int level ) {
	if(exp != null)
	{	
		//level++;

		//left hand side stuff
		if(exp.lhs instanceof SimpleVar)
		{
			//System.out.println("***************	lhs simpleVar");
			//TODO
			VarExp tempVarExp = new VarExp(0,0,exp.lhs);
			SimpleVar tempVar = (SimpleVar)(tempVarExp.variable);
			SimpleDec tempDec = tempVar.simpleDecPointer;
			emitRM("LDA", 0, tempDec.offset ,5, "loading varExp" );
			
			//Storing
			emitRM("ST", 0, newframeOffset(), 5, "store value" );
		}
		else if(exp.lhs instanceof IndexVar)
		{
		}	
		
		exp.rhs.accept( this, level--);
		
		/*
		//right hand side stuff
		if(exp.rhs instanceof VarExp){
			//TODO
			emitRM("ST", 0, 0, 1, "assign: store value );
		}
		else if(exp.rhs instanceof IntExp)
		{
			emitRM("ST", 0, 0, 1, "assign: store value of int" );
		}
		else if(exp.rhs instanceof OpExp)
		{

		}
		*/
		emitRM("ST", 0, 0, 1, "assign: store value" );

	}
  }

  public void visit( IfExp exp, int level ) {
	if(exp != null)
	{	
		int saveLine = emitLoc++;
		exp.test.accept( this, level--);
		
		exp.thenpart.accept( this, level--);
		emitRM("JEQ", 0, (emitLoc - saveLine), 7, "br if true");
		
		int saveLine2 = emitLoc++;
		
		if (exp.elsepart != null )
		{
		   exp.elsepart.accept( this, level--);
		   emitRM("LDA", 7, (emitLoc - saveLine2 - 1), 7, "jump");
		}
   }
   //System.out.println("ifExp");
  }

  public void visit( IntExp exp, int level ) {
	if(exp != null)
	{	
		emitRM("LDC", 0, exp.value, 0, "loading int" );
	}
  }

public void visit( OpExp exp, int level ) {
     if(exp != null)
    {
        //level++;
        exp.left.accept( this, level-- );
        exp.right.accept( this, level-- );
        //System.out.println("opExp");

        int operation = exp.op;
        String opCode = "";

        if (operation == OpExp.DIV)
        {
            opCode = "DIV";
        }
        else if (operation == OpExp.MINUS)
        {
            opCode = "SUB";
        }
        else if (operation == OpExp.MUL)
        {
            opCode = "MUL";
        }
        else if (operation == OpExp.PLUS)
        {
            opCode = "ADD";
        }
        emitRM("LD",1, frameOffset,5,"op load left");
        emitRO(opCode, 0, 1, 0, "perform operation " + opCode);
        // only do this LD after math ops not after comparison ops
        int prevOffset = frameOffset - 1;
        emitRM("LD",1, prevOffset,5,"op load left");
    }
  }

  public void visit( VarExp exp, int level ) {
	  if(exp != null)
	{	
		//level++;
		//this will either be an indexVar or a simpleVar
		exp.variable.accept ( this, level);    
		//System.out.println("varExp");
	}
  }

  public void visit( CallExp exp, int level ) {
	  if(exp != null)
	{	
		//level++;
		visit(exp.args, level--);
		//System.out.println("callExp");
	}
  }
  
  public void visit( CompoundExp exp, int level ) {
	 if(exp != null)
	{	
		//level++;
		visit(exp.varDecList, level--);
		visit(exp.expList, level--);
		//System.out.println("compundExp");
	}
  }
  
  public void visit( IndexVar iVar, int level ) {
	  if(iVar != null)
	{	
		//level++;
		//this is an exp
		String codestr = "";
		iVar.index.accept( this, level--);
		//System.out.println("indexVar");
	}
  }

  public void visit( SimpleVar sVar, int level ) {
	if(sVar != null)
	{	
			SimpleDec tempDec = sVar.simpleDecPointer;
			emitRM("LDA", 0, tempDec.offset ,5, "loading simpleVar" );
			
			//Storing
			emitRM("ST", 0, newframeOffset(), 5, "store value" );
	}
  }
  
  public void visit( ReturnExp rExp, int level ) {
	  if(rExp != null)
	{	
		//level++;
		rExp.exp.accept( this, level);
		//System.out.println("returnExp");
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
		int saveLine1 = emitLoc++;
		exp.test.accept( this, level--);
		int saveLine2 = emitLoc++;
		exp.block.accept( this, level--);
		
		
		emitRM("JGT", 0, (saveLine1-emitLoc) ,7, "br if true" );
		emitRM("LDC", 0, 0 ,0, "false case" );
		emitRM("LDA", 7, (saveLine2-emitLoc) ,7, "jump" );
		emitRM("LDC", 0, 1 ,0, "true case" );
		//System.out.println("whileExp");
	}
  }
  
  public void visit( ArrayDec exp, int level ) {
	  if(exp != null)
	{	
		//exp.size.accept( this, level);
		visit(exp.typ, level);
		exp.offset = newframeOffset();
		
		exp.nestLevel = level;
		//System.out.println("arrayDec");
	}
  }
  
  public void visit( FunctionDec exp, int level ) {
	if(exp != null)
	{	
		if(exp.func.equals("main"))
		{
			entry = emitLoc;
		}
		exp.functionAddr = emitLoc;
		emitLoc++;
		emitRM("ST", 0, -1, 5, "store return address");


		exp.result.accept( this, level);
		//level call param list
		exp.params.accept(this, level);
		//level call body
		exp.body.accept(this, level--);
		//return
		//not sure if this is always -1 for the b
		emitRM("LD", 7, -1, 5, "return back to caller");
		System.out.println(exp.functionAddr + ":	" + "LDA 7, " +  (emitLoc - exp.functionAddr -1)  + "(7)" + " " + "jump forward");
		frameOffset = -1;
		
		
		//System.out.println("functionDec");
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
		exp.offset = newframeOffset();
		exp.nestLevel = level;
		//System.out.println("simpleDec " + exp.offset);
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
	
	public void emitBackup( int loc) 
	{
		if(loc > highEmitLoc)
		{
			emitComment("BUG in emitBackup"); 
		}
		emitLoc = loc;
	} 
	
	public void emitRestore() 
	{
		emitLoc= highEmitLoc;
	}
	
	public void emitRM_Abs( String op, int r, int a, String c ) 
	{
		System.out.println(emitLoc + ":	" + op +  " " + r + ", " + (a - (emitLoc + 1)) + "(" + r + ")");
		emitLoc++;
		if( TraceCode) 
			System.out.println("	" + c );
		if( highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;
	}
	
  private void emitRM(String op, int a, int b, int c, String stuff )
	{
	  System.out.println(emitLoc + ":	" + op +  " " + a + ", " + b + "(" + c + ")" + " " + stuff);	  
	  emitLoc++;
	  if( highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;
  }

  private void emitRO(String op, int a, int b, int c, String stuff )
  {
	  System.out.println(emitLoc + ":	" + op +  " " + a + ", " + b + "," + c + " " + stuff);
	  emitLoc++;
	  if( highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;
  }
}
