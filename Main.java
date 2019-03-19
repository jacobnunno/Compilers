/*
  Created by: Ahsen Husain, Giacomo Nunno
  File Name: Main.java
*/
   
import java.io.*;
import absyn.*;
   
class Main {
  public static boolean SHOW_TREE = false;
  public static boolean SHOW_TABLE = false;
  static public void main(String argv[]) throws FileNotFoundException {    
    
    try {
      //redirect sys out to file
      String file = "syntaxTree.txt";
	  PrintStream o = new PrintStream(new File(file)); 
      PrintStream console = System.out; 
      System.setOut(o); 
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
      }    
      if (SHOW_TREE) {
         System.out.println("The abstract syntax tree is:");
         ShowTreeVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0); 
      }
      if(SHOW_TABLE){
		  System.out.println("The Symbol Tree is:");
		  SemanticAnalyzer builder = new SemanticAnalyzer();
          result.accept(builder, 0);
	  }
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

