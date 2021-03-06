package Latte.Absyn; // Java Package generated by the BNF Converter.

public class Prog extends Program {
  public final ListTopDef listtopdef_;
  public Prog(ListTopDef p1) { listtopdef_ = p1; }

  public <R,A> R accept(Latte.Absyn.Program.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.Prog) {
      Latte.Absyn.Prog x = (Latte.Absyn.Prog)o;
      return this.listtopdef_.equals(x.listtopdef_);
    }
    return false;
  }

  public int hashCode() {
    return this.listtopdef_.hashCode();
  }


}
