package Latte.Absyn; // Java Package generated by the BNF Converter.

public abstract class MethodCall implements java.io.Serializable {
  public abstract <R,A> R accept(MethodCall.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(Latte.Absyn.MCall p, A arg);

  }

}
