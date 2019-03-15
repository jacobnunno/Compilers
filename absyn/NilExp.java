package absyn;

/*NilExp is typically used for the empty return statement and the else-branch 
 * when the else part is empty.  It's a better alternative than "null" since we can 
 * also show its row and column numbers if needed.*/

public class NilExp extends Exp {

  public NilExp( int row, int col) {
    this.row = row;
    this.col = col;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
