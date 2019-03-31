package absyn;

public class SimpleVar extends Var {
  public String name;
  
  public SimpleDec simpleVarPointer;

  public SimpleVar( int row, int col, String name ) {
    this.row = row;
    this.col = col;
    this.name = name;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
    public void accept( SemanticAnalyzerBuilder builder, int level ) {
    builder.build( this, level );
  }
}
