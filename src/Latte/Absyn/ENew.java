package Latte.Absyn; // Java Package generated by the BNF Converter.

public class ENew extends Expr {
  public final String ident_;
  public ENew(String p1) { ident_ = p1; }

  public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.ENew) {
      Latte.Absyn.ENew x = (Latte.Absyn.ENew)o;
      return this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return this.ident_.hashCode();
  }


}
