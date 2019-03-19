package absyn;

public interface SemanticAnalyzerBuilder {

  public void build( ExpList exp, int level );

  public void build( DecList exp, int level  );
  
  public void build( VarDecList exp, int level  );
  
  public void build( AssignExp exp, int level  );

  public void build( IfExp exp, int level  );

  public void build( IntExp exp, int level  );

  public void build( OpExp exp, int level  );

  public void build( VarExp exp, int level  );
  
  public void build( CallExp exp, int level  );
  
  public void build( CompoundExp exp, int level  );
  
  public void build( IndexVar exp, int level  );
  
  public void build( SimpleVar exp, int level  );
  
  public void build( ReturnExp exp, int level  );
  
  public void build( NilExp exp, int level  );
  
  public void build( WhileExp exp, int level  );
  
  public void build( ArrayDec exp, int level  );
  
  public void build( FunctionDec exp, int level  );
  
  public void build( NameTy exp, int level  );
  
  public void build( SimpleDec exp, int level  );
  
  public void build( ErrorExp exp, int level  );
  
  public void build( ErrorVarDec exp, int level  );
  
  public void build( ErrorDec exp, int level  );
  
}
