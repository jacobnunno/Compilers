package absyn;

public class NameTy extends Absyn {
  public final static int INT  = 1;
  public final static int VOID = 0;
  public int typ;
	
  public NameTy( int row, int col, int typ) {
    this.row = row;
    this.col = col;
    this.typ = typ;
  }
  
  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
    public void accept( SemanticAnalyzerBuilder builder, int level, int scope ) {
    builder.build( this, level, scope );
  }
}
