package Latte.Absyn; // Java Package generated by the BNF Converter.

public abstract class ArrAccess implements java.io.Serializable {
  public abstract <R,A> R accept(ArrAccess.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(Latte.Absyn.ArrAcc p, A arg);

  }

}
