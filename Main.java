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
      }
      //Checkpoint 2 
      //set the output to console
      System.setOut(console);
      //reading the tree line by line
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				
				System.out.println(line);
			}
		}
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


