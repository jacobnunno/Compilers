package absyn;

public class ExpList extends Absyn {
  public Exp head;
  public ExpList tail;

  public ExpList( Exp head, ExpList tail ) {
    this.head = head;
    this.tail = tail;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
    public void accept( SemanticAnalyzerBuilder builder, int level ) {
    builder.build( this, level );
  }
  
  public int getSize()
  {
	 int size = 1;
	 if(this.head != null && this.tail != null)
	 {
		 while( this.tail != null ) 
		 {
			  size ++;
			  this.tail = this.tail.tail;
		 } 	 
	 }
    return size;
  }
}
