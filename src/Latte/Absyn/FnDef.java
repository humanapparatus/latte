package Latte.Absyn; // Java Package generated by the BNF Converter.

public class FnDef extends TopDef {
  public final FuncDef funcdef_;
  public FnDef(FuncDef p1) { funcdef_ = p1; }

  public <R,A> R accept(Latte.Absyn.TopDef.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.FnDef) {
      Latte.Absyn.FnDef x = (Latte.Absyn.FnDef)o;
      return this.funcdef_.equals(x.funcdef_);
    }
    return false;
  }

  public int hashCode() {
    return this.funcdef_.hashCode();
  }


}
