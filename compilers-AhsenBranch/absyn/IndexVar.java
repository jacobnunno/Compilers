package absyn;

public class IndexVar extends Var {
  public String name;
  public Exp index;

  public IndexVar( int row, int col, String name, Exp index ) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.index = index;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
    public void accept( SemanticAnalyzerBuilder builder, int level ) {
    builder.build( this, level );
  }
}
