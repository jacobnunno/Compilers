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
  //public int CHANGETHIS = -1;
  
  //not sure if it should init as true or false
  public boolean TraceCode = false;
  public int entry = 0;
  public boolean lhsAssignFlag= false;
  
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
  
  public void visit( ExpList expList, int frameOffset ) {
    while( expList != null && expList.head != null) {
      expList.head.accept( this, frameOffset-- );
      expList = expList.tail;
    } 
  }
  
public void visit( DecList decList, int frameOffset ) {
	System.out.println("* Standard prelude");
	//prelude

	System.out.println("0: LD  6,0(0)");
	System.out.println("1: LDA  5,0(6)");
	System.out.println("2: ST  0,0(0)");
	System.out.println("4: ST  0,-1(5)");
	System.out.println("5: IN  0,0,0");
	System.out.println("6: LD  7,-1(5)");
	System.out.println("7: ST  0,-1(5)");
	System.out.println("8: LD  0,-2(5)");
	System. out.println("9: OUT  0,0,0");
	System. out.println("10: LD  7,-1(5)");
	System.out.println("3: LDA  7,7(7)");
	emitLoc = 11;
      
      
    //generating code  
      
    while( decList != null ) {
      decList.head.accept( this, frameOffset );
      decList = decList.tail;
    } 
    
    //end of execution
    emitRM( "ST", 5, globalOffset+0, 5, "push ofp" );
    emitRM( "LDA", 5, globalOffset, 5, "push frame" );
    emitRM( "LDA", 0, 1, 7, "load ac with ret ptr" );
    emitRM_Abs( "LDA", 7, entry+1, "jump to main loc" );
    emitRM( "LD", 5, 0, 5, "pop frame" );
    emitRO( "HALT", 0, 0, 0, "" );
  }
  
  public void visit( VarDecList varDecList, int frameOffset ) {
    while( varDecList != null && varDecList.head != null) {
      varDecList.head.accept( this, frameOffset--);
      varDecList = varDecList.tail;
    } 
  }

  public void visit( AssignExp exp, int frameOffset ) {
	if(exp != null)
	{	
		//level++;
	System.out.println("*************OFFSET CHECK " + frameOffset + " frameoffset AssignEXP" );
		//left hand side stuff
		if(exp.lhs instanceof SimpleVar)
		{
			lhsAssignFlag = true;
		}
		exp.lhs.accept( this, frameOffset - 1);
		
		lhsAssignFlag =false;
		
		if(exp.lhs instanceof SimpleVar)
		{
		}
		else if(exp.lhs instanceof IndexVar)
		{
		}	
		
		exp.rhs.accept( this, frameOffset - 2);
		
		if(exp.rhs instanceof OpExp)
		{
			emitRM("LD", 0, frameOffset - 1, 5, " loading result" );
			emitRM("LD", 1, frameOffset - 2, 5, "loading result" );
			emitRM("ST", 1, 0, 0, "assign: store value" );
			emitRM("ST", 1, frameOffset, 5, "" );
		}
		

	}
  }

  public void visit( IfExp exp, int frameOffset ) {
	if(exp != null)
	{	
		int saveLine = emitLoc++;
		exp.test.accept( this, frameOffset--);
		
		exp.thenpart.accept( this, frameOffset--);
		emitRM("JEQ", 0, (emitLoc - saveLine), 7, "br if true");
		
		int saveLine2 = emitLoc++;
		
		if (exp.elsepart != null )
		{
		   exp.elsepart.accept( this, frameOffset--);
		   emitRM("LDA", 7, (emitLoc - saveLine2 - 1), 7, "jump");
		}
   }
   //System.out.println("ifExp");
  }

  public void visit( IntExp exp, int frameOffset ) {
	if(exp != null)
	{	
		System.out.println("*************OFFSET CHECK " + frameOffset + "frameoffset INTEXP" );
		emitRM("LDC", 0, exp.value, 0, "loading int" );
		emitRM("ST", 0, frameOffset, 5, "store value" );
	}
  }

public void visit( OpExp exp, int frameOffset ) {
     if(exp != null)
    {
		System.out.println("*************OFFSET CHECK " + frameOffset + "OPEXP" );
        //level++;
        exp.left.accept( this, frameOffset - 1 );
        exp.right.accept( this, frameOffset -2 );
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
        emitRM("LD",0, frameOffset - 1,5,"load operand in register 0");
        emitRM("LD",1, frameOffset - 2,5,"load operand in register 1");
        emitRO(opCode, 0, 0, 1, "perform operation " + opCode);
        emitRM( "ST", 0, frameOffset, 5, "store OpExp" );
        // only do this LD after math ops not after comparison ops
    }
  }

  public void visit( VarExp exp, int frameOffset ) {
	  if(exp != null)
	{	
		//level++;
		//this will either be an indexVar or a simpleVar
		exp.variable.accept ( this, frameOffset);    
		//System.out.println("varExp");
	}
  }

  public void visit( CallExp exp, int frameOffset ) {
	  if(exp != null)
	{	
		//level++;
		visit(exp.args, frameOffset--);
		System.out.println("*************CALL EXP" + frameOffset + " ");
		FunctionDec callFunDec = exp.functionPointer;
		
		emitRM( "ST", 5, frameOffset, 5, "push ofp" );
		emitRM( "LDA", 5, frameOffset, 5, "push frame" );
		emitRM( "LDA", 0, 1, 7, "load ac with ret ptr" );
		emitRM_Abs( "LDA", 7, callFunDec.functionAddr + 1, "jump to main loc" );
		emitRM( "LD", 5, 0, 5, "pop frame" );
		//System.out.println("callExp");
	}
  }
  
  public void visit( CompoundExp exp, int frameOffset ) {
	 if(exp != null)
	{	
		//level++;
		visit(exp.varDecList, frameOffset--);
		visit(exp.expList, frameOffset--);
		//System.out.println("compundExp");
	}
  }
  
  public void visit( IndexVar iVar, int frameOffset ) {
	  if(iVar != null)
	{	
		//level++;
		//this is an exp
		String codestr = "";
		iVar.index.accept( this, frameOffset--);
		//System.out.println("indexVar");
	}
  }

  public void visit( SimpleVar sVar, int frameOffset ) {
	if(sVar != null)
	{	
		System.out.println("*************OFFSET CHECK " + frameOffset + "SIMPLEVAR" );
			SimpleDec tempDec = sVar.simpleDecPointer;
			if(lhsAssignFlag)
			{
				emitRM("LDA", 0, tempDec.offset ,5, "loading simpleVar" );
			}
			else
			{
				emitRM("LD", 0, tempDec.offset ,5, "loading simpleVar" );
			}
			//emitRM("***********************THING" + sVar.name, 0, 0 ,5, "loading simpleVar" );
			
			//Storing
			emitRM("ST", 0, frameOffset, 5, "store value" );
	}
  }
  
  public void visit( ReturnExp rExp, int frameOffset ) {
	  if(rExp != null)
	{	
		//level++;
		rExp.exp.accept( this, frameOffset);
		//System.out.println("returnExp");
	}
  }
  
  public void visit( NilExp nExp, int frameOffset ) {
	  if(nExp != null)
	{	
		
	}
  }

  public void visit( WhileExp exp, int frameOffset ) {
	  if(exp != null)
	{	
		//level++;
		int saveLine1 = emitLoc++;
		exp.test.accept( this, frameOffset--);
		int saveLine2 = emitLoc++;
		exp.block.accept( this, frameOffset--);
		
		
		emitRM("JGT", 0, (saveLine1-emitLoc) ,7, "br if true" );
		emitRM("LDC", 0, 0 ,0, "false case" );
		emitRM("LDA", 7, (saveLine2-emitLoc) ,7, "jump" );
		emitRM("LDC", 0, 1 ,0, "true case" );
		//System.out.println("whileExp");
	}
  }
  
  public void visit( ArrayDec exp, int frameOffset ) {
	  if(exp != null)
	{	
		//exp.size.accept( this, frameOffset);
		visit(exp.typ, frameOffset);
		exp.offset = frameOffset;
		
		exp.nestLevel = frameOffset;
		//System.out.println("arrayDec");
	}
  }
  
  public void visit( FunctionDec exp, int frameOffset ) {
	if(exp != null)
	{	
		if(exp.func.equals("main"))
		{
			entry = emitLoc;
		}
		exp.functionAddr = emitLoc;
		emitLoc++;
		emitRM("ST", 0, -1, 5, "store return address");


		exp.result.accept( this, frameOffset);
		//level call param list
		exp.params.accept(this, frameOffset);
		//level call body
		exp.body.accept(this, frameOffset--);
		//return
		//not sure if this is always -1 for the b
		emitRM("LD", 7, -1, 5, "return back to caller");
		System.out.println(exp.functionAddr + ":	" + "LDA 7, " +  (emitLoc - exp.functionAddr -1)  + "(7)" + " " + "jump forward");
		
		
		//System.out.println("functionDec");
	}
  }
  
  public void visit( NameTy exp, int frameOffset ) {
	if(exp != null)
	{	
		
	}
  }
  
  public void visit( SimpleDec exp, int frameOffset ) {
	if(exp != null)
	{	
		visit(exp.typ, frameOffset);
		exp.offset = frameOffset;
		exp.nestLevel = frameOffset;
		//System.out.println("simpleDec " + exp.offset);
	}
  }
  
  public void visit( ErrorExp err, int frameOffset ) {
	  if(err != null)
	{	
	}
  }
  
  public void visit( ErrorDec err, int frameOffset ) {
	  if(err != null)
	{	
		
	}
  }
  
  public void visit( ErrorVarDec err, int frameOffset ) {
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
		System.out.println(emitLoc + ":	" + op +  "  " + r + "," + (a - (emitLoc + 1)) + "(" + r + ")");
		emitLoc++;
		if( TraceCode) 
			System.out.println("	" + c );
		if( highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;
	}
	
  private void emitRM(String op, int a, int b, int c, String stuff )
	{
	  System.out.println(emitLoc + ": " + op +  "  " + a + "," + b + "(" + c + ")" + " " + stuff);	  
	  emitLoc++;
	  if( highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;
  }

  private void emitRO(String op, int a, int b, int c, String stuff )
  {
	  System.out.println(emitLoc + ": " + op +  "  " + a + "," + b + "," + c + " " + stuff);
	  emitLoc++;
	  if( highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;
  }
}
