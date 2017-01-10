package Latte.Absyn; // Java Package generated by the BNF Converter.

public class Obj extends Type {
  public final String ident_;
  public Obj(String p1) { ident_ = p1; }

  public <R,A> R accept(Latte.Absyn.Type.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.Obj) {
      Latte.Absyn.Obj x = (Latte.Absyn.Obj)o;
      return this.ident_.equals(x.ident_);
    }
    return false;
  }

  public int hashCode() {
    return this.ident_.hashCode();
  }


}
