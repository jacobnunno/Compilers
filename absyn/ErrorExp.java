package absyn;

public class ErrorExp extends Exp {

  public ErrorExp( int row, int col) {
    this.row = row;
    this.col = col;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
    public void accept( SemanticAnalyzerBuilder builder, int level, int scope ) {
    builder.build( this, level, scope );
  }
}
