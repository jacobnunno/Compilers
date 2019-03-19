/*
  Created by: Ahsen Husain, Giacomo Nunno
  File Name: Main.java
*/
   
import java.io.*;
import absyn.*;
   
class Main {
  public final static boolean SHOW_TREE = true;
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
      if (SHOW_TREE) {
         System.out.println("The abstract syntax tree is:");
         ShowTreeVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0); 
         
         SemanticAnalyzer builder = new SemanticAnalyzer();
         result.accept(builder, 0, 0);
      }
	  //Checkpoint 2
	  System.setOut(console); 
      boolean sFlag = false;
      for(int ctr = 0; ctr < argv.length; ctr ++)
      {
          if(argv[ctr].equals("-s"))
          {
                sFlag = true;
          }
      }
      //pass tree to semantic analyzer
	  //new SemanticAnalyzer(result, sFlag);
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

