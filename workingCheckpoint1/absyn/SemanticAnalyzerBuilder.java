package absyn;

public interface SemanticAnalyzerBuilder {

  public void build( ExpList exp, int level, int scope );

  public void build( DecList exp, int level, int scope  );
  
  public void build( VarDecList exp, int level, int scope  );
  
  public void build( AssignExp exp, int level, int scope  );

  public void build( IfExp exp, int level, int scope  );

  public void build( IntExp exp, int level, int scope  );

  public void build( OpExp exp, int level, int scope  );

  public void build( VarExp exp, int level, int scope  );
  
  public void build( CallExp exp, int level, int scope  );
  
  public void build( CompoundExp exp, int level, int scope  );
  
  public void build( IndexVar exp, int level, int scope  );
  
  public void build( SimpleVar exp, int level, int scope  );
  
  public void build( ReturnExp exp, int level, int scope  );
  
  public void build( NilExp exp, int level, int scope  );
  
  public void build( WhileExp exp, int level, int scope  );
  
  public void build( ArrayDec exp, int level, int scope  );
  
  public void build( FunctionDec exp, int level, int scope  );
  
  public void build( NameTy exp, int level, int scope  );
  
  public void build( SimpleDec exp, int level, int scope  );
  
  public void build( ErrorExp exp, int level, int scope  );
  
  public void build( ErrorVarDec exp, int level, int scope  );
  
  public void build( ErrorDec exp, int level, int scope  );
  
}
