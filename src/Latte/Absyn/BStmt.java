package Latte.Absyn; // Java Package generated by the BNF Converter.

public class BStmt extends Stmt {
  public final Block block_;
  public BStmt(Block p1) { block_ = p1; }

  public <R,A> R accept(Latte.Absyn.Stmt.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.BStmt) {
      Latte.Absyn.BStmt x = (Latte.Absyn.BStmt)o;
      return this.block_.equals(x.block_);
    }
    return false;
  }

  public int hashCode() {
    return this.block_.hashCode();
  }


}
