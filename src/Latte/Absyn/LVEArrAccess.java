package Latte.Absyn; // Java Package generated by the BNF Converter.

public class LVEArrAccess extends LValue {
  public final ArrAccess arraccess_;
  public LVEArrAccess(ArrAccess p1) { arraccess_ = p1; }

  public <R,A> R accept(Latte.Absyn.LValue.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.LVEArrAccess) {
      Latte.Absyn.LVEArrAccess x = (Latte.Absyn.LVEArrAccess)o;
      return this.arraccess_.equals(x.arraccess_);
    }
    return false;
  }

  public int hashCode() {
    return this.arraccess_.hashCode();
  }


}
