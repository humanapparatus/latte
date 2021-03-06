package Latte.Absyn; // Java Package generated by the BNF Converter.

public abstract class ClassElem implements java.io.Serializable {
  public abstract <R,A> R accept(ClassElem.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(Latte.Absyn.ClassMeth p, A arg);
    public R visit(Latte.Absyn.ClassAtr p, A arg);

  }

}
