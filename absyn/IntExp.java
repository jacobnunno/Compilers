package absyn;

public class IntExp extends Exp {
  public String value;

  public IntExp( int row, int col, String value ) {
    this.row = row;
    this.col = col;
    this.value = value;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
    public void accept( SemanticAnalyzerBuilder builder, int level, int scope ) {
    builder.build( this, level, scope );
  }
}
