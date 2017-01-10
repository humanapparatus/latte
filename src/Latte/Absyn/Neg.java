package Latte.Absyn; // Java Package generated by the BNF Converter.

public class Neg extends Expr {
  public final Expr expr_;
  public Neg(Expr p1) { expr_ = p1; }

  public <R,A> R accept(Latte.Absyn.Expr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.Neg) {
      Latte.Absyn.Neg x = (Latte.Absyn.Neg)o;
      return this.expr_.equals(x.expr_);
    }
    return false;
  }

  public int hashCode() {
    return this.expr_.hashCode();
  }


}
