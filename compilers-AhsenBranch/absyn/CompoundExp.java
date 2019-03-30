package absyn;

public class CompoundExp extends Exp {
  public VarDecList varDecList;
  public ExpList expList;

  public CompoundExp( int row, int col, VarDecList varDecList, ExpList expList) {
    this.row = row;
    this.col = col;
    this.varDecList = varDecList;
    this.expList = expList;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  
    public void accept( SemanticAnalyzerBuilder builder, int level ) {
    builder.build( this, level );
  }
}

