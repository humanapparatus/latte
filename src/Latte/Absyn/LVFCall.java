package Latte.Absyn; // Java Package generated by the BNF Converter.

public class LVFCall extends LValue {
  public final FunctionCall functioncall_;
  public LVFCall(FunctionCall p1) { functioncall_ = p1; }

  public <R,A> R accept(Latte.Absyn.LValue.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Latte.Absyn.LVFCall) {
      Latte.Absyn.LVFCall x = (Latte.Absyn.LVFCall)o;
      return this.functioncall_.equals(x.functioncall_);
    }
    return false;
  }

  public int hashCode() {
    return this.functioncall_.hashCode();
  }


}
