/*
  Created by: Ahsen Husain, Giacomo Nunno
  File Name: Main.java
*/
   
import java.io.*;
import absyn.*;
   
class Main {
  public static boolean SHOW_TREE = false;
  public static boolean SHOW_TABLE = false;
  public static boolean SHOW_CODE = false;
  static public void main(String argv[]) throws FileNotFoundException {    
    
    try {
      //redirect sys out to file
      String file = "syntaxTree.txt";
	  PrintStream tree = new PrintStream(new File(file)); 
	  String file2 = "symbolTable.txt";
	  PrintStream table = new PrintStream(new File(file2)); 
      PrintStream console = System.out; 
      String file3 = "zAssemblyCode.txt";
	  PrintStream code = new PrintStream(new File(file3)); 
      System.setOut(tree); 
      /* Start the parser */
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);  
      
      //Checkpoint 2

      for(int ctr = 0; ctr < argv.length; ctr ++)
      {
          if(argv[ctr].equals("-s"))
          {
                SHOW_TABLE = true;
          }
          if(argv[ctr].equals("-a"))
          {
                SHOW_TREE = true;
          }
          if(argv[ctr].equals("-c"))
          {
			  SHOW_CODE = true;
		  }
      }    
      if (SHOW_TREE) {
         System.out.println("The abstract syntax tree is:");
         ShowTreeVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0); 
      }
      if(SHOW_TABLE){
		  System.setOut(table); 
		  System.out.println("The Symbol Tree is:");
		  SemanticAnalyzer builder = new SemanticAnalyzer();
          result.accept(builder, 0);
	  }
	  if (SHOW_CODE)
	  {
		  System.setOut(code); 
		  System.out.println("The Code Generated is:");
		  CodeGenerator generator = new CodeGenerator();
          result.accept(generator, 0);
	  }
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

