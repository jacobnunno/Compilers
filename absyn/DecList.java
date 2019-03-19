package absyn;

public class DecList extends Absyn {
  public Dec head;
  public DecList tail;

  public DecList( Dec head, DecList tail ) {
    this.head = head;
    this.tail = tail;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
  
    public void accept( SemanticAnalyzerBuilder builder, int level ) {
    builder.build( this, level );
  }
}
