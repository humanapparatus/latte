package Latte;
import Latte.Absyn.*;
/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use. 
   Replace the R and A parameters with the desired return
   and context types.*/

public class VisitSkel
{
  public class ProgramVisitor<R,A> implements Program.Visitor<R,A>
  {
    public R visit(Latte.Absyn.Prog p, A arg)
    { /* Code For Prog Goes Here */
      for (TopDef x: p.listtopdef_)
      { /* ... */ }
      return null;
    }
  }
  public class TopDefVisitor<R,A> implements TopDef.Visitor<R,A>
  {
    public R visit(Latte.Absyn.FnDef p, A arg)
    { /* Code For FnDef Goes Here */
      p.funcdef_.accept(new FuncDefVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.ClassDef p, A arg)
    { /* Code For ClassDef Goes Here */
      //p.ident_;
      p.classbody_.accept(new ClassBodyVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.ClassDefE p, A arg)
    { /* Code For ClassDefE Goes Here */
      //p.ident_1;
      //p.ident_2;
      p.classbody_.accept(new ClassBodyVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ArgVisitor<R,A> implements Arg.Visitor<R,A>
  {
    public R visit(Latte.Absyn.Ar p, A arg)
    { /* Code For Ar Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      return null;
    }
  }
  public class FuncDefVisitor<R,A> implements FuncDef.Visitor<R,A>
  {
    public R visit(Latte.Absyn.FunDef p, A arg)
    { /* Code For FunDef Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      for (Arg x: p.listarg_)
      { /* ... */ }
      p.block_.accept(new BlockVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ClassBodyVisitor<R,A> implements ClassBody.Visitor<R,A>
  {
    public R visit(Latte.Absyn.ClassBlock p, A arg)
    { /* Code For ClassBlock Goes Here */
      for (ClassElem x: p.listclasselem_)
      { /* ... */ }
      return null;
    }
  }
  public class ClassElemVisitor<R,A> implements ClassElem.Visitor<R,A>
  {
    public R visit(Latte.Absyn.ClassMeth p, A arg)
    { /* Code For ClassMeth Goes Here */
      p.funcdef_.accept(new FuncDefVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.ClassAtr p, A arg)
    { /* Code For ClassAtr Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;
      return null;
    }
  }
  public class BlockVisitor<R,A> implements Block.Visitor<R,A>
  {
    public R visit(Latte.Absyn.Blk p, A arg)
    { /* Code For Blk Goes Here */
      for (Stmt x: p.liststmt_)
      { /* ... */ }
      return null;
    }
  }
  public class StmtVisitor<R,A> implements Stmt.Visitor<R,A>
  {
    public R visit(Latte.Absyn.Empty p, A arg)
    { /* Code For Empty Goes Here */
      return null;
    }    public R visit(Latte.Absyn.BStmt p, A arg)
    { /* Code For BStmt Goes Here */
      p.block_.accept(new BlockVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.Decl p, A arg)
    { /* Code For Decl Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      for (Item x: p.listitem_)
      { /* ... */ }
      return null;
    }    public R visit(Latte.Absyn.Ass p, A arg)
    { /* Code For Ass Goes Here */
      p.lvalue_.accept(new LValueVisitor<R,A>(), arg);
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.Incr p, A arg)
    { /* Code For Incr Goes Here */
      p.lvalue_.accept(new LValueVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.Decr p, A arg)
    { /* Code For Decr Goes Here */
      p.lvalue_.accept(new LValueVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.Ret p, A arg)
    { /* Code For Ret Goes Here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.VRet p, A arg)
    { /* Code For VRet Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Cond p, A arg)
    { /* Code For Cond Goes Here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.stmt_.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.CondElse p, A arg)
    { /* Code For CondElse Goes Here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.stmt_1.accept(new StmtVisitor<R,A>(), arg);
      p.stmt_2.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.While p, A arg)
    { /* Code For While Goes Here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      p.stmt_.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.For p, A arg)
    { /* Code For For Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_1;
      //p.ident_2;
      p.stmt_.accept(new StmtVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.SExp p, A arg)
    { /* Code For SExp Goes Here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class ItemVisitor<R,A> implements Item.Visitor<R,A>
  {
    public R visit(Latte.Absyn.NoInit p, A arg)
    { /* Code For NoInit Goes Here */
      //p.ident_;
      return null;
    }    public R visit(Latte.Absyn.Init p, A arg)
    { /* Code For Init Goes Here */
      //p.ident_;
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class TypeVisitor<R,A> implements Type.Visitor<R,A>
  {
    public R visit(Latte.Absyn.Arr p, A arg)
    { /* Code For Arr Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }        public R visit(Latte.Absyn.Int p, A arg)
    { /* Code For Int Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Str p, A arg)
    { /* Code For Str Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Bool p, A arg)
    { /* Code For Bool Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Void p, A arg)
    { /* Code For Void Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Obj p, A arg)
    { /* Code For Obj Goes Here */
      //p.ident_;
      return null;
    }
  }
  public class ExprVisitor<R,A> implements Expr.Visitor<R,A>
  {
    public R visit(Latte.Absyn.ECastNull p, A arg)
    { /* Code For ECastNull Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.EAttrAccess p, A arg)
    { /* Code For EAttrAccess Goes Here */
      p.attraccess_.accept(new AttrAccessVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.EMthCall p, A arg)
    { /* Code For EMthCall Goes Here */
      p.methodcall_.accept(new MethodCallVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.EArrAccess p, A arg)
    { /* Code For EArrAccess Goes Here */
      p.arraccess_.accept(new ArrAccessVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.EVar p, A arg)
    { /* Code For EVar Goes Here */
      //p.ident_;
      return null;
    }    public R visit(Latte.Absyn.ENewArr p, A arg)
    { /* Code For ENewArr Goes Here */
      p.type_.accept(new TypeVisitor<R,A>(), arg);
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.ENew p, A arg)
    { /* Code For ENew Goes Here */
      //p.ident_;
      return null;
    }    public R visit(Latte.Absyn.ELitInt p, A arg)
    { /* Code For ELitInt Goes Here */
      //p.integer_;
      return null;
    }    public R visit(Latte.Absyn.ELitTrue p, A arg)
    { /* Code For ELitTrue Goes Here */
      return null;
    }    public R visit(Latte.Absyn.ELitFalse p, A arg)
    { /* Code For ELitFalse Goes Here */
      return null;
    }    public R visit(Latte.Absyn.EApp p, A arg)
    { /* Code For EApp Goes Here */
      p.functioncall_.accept(new FunctionCallVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.EString p, A arg)
    { /* Code For EString Goes Here */
      //p.string_;
      return null;
    }        public R visit(Latte.Absyn.Neg p, A arg)
    { /* Code For Neg Goes Here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.Not p, A arg)
    { /* Code For Not Goes Here */
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }        public R visit(Latte.Absyn.EMul p, A arg)
    { /* Code For EMul Goes Here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.mulop_.accept(new MulOpVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }        public R visit(Latte.Absyn.EAdd p, A arg)
    { /* Code For EAdd Goes Here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.addop_.accept(new AddOpVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }        public R visit(Latte.Absyn.ERel p, A arg)
    { /* Code For ERel Goes Here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.relop_.accept(new RelOpVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }        public R visit(Latte.Absyn.EAnd p, A arg)
    { /* Code For EAnd Goes Here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }        public R visit(Latte.Absyn.EOr p, A arg)
    { /* Code For EOr Goes Here */
      p.expr_1.accept(new ExprVisitor<R,A>(), arg);
      p.expr_2.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }    
  }
  public class FunctionCallVisitor<R,A> implements FunctionCall.Visitor<R,A>
  {
    public R visit(Latte.Absyn.FCall p, A arg)
    { /* Code For FCall Goes Here */
      //p.ident_;
      for (Expr x: p.listexpr_)
      { /* ... */ }
      return null;
    }
  }
  public class ArrAccessVisitor<R,A> implements ArrAccess.Visitor<R,A>
  {
    public R visit(Latte.Absyn.ArrAcc p, A arg)
    { /* Code For ArrAcc Goes Here */
      p.lvalue_.accept(new LValueVisitor<R,A>(), arg);
      p.expr_.accept(new ExprVisitor<R,A>(), arg);
      return null;
    }
  }
  public class AttrAccessVisitor<R,A> implements AttrAccess.Visitor<R,A>
  {
    public R visit(Latte.Absyn.AttrAcc p, A arg)
    { /* Code For AttrAcc Goes Here */
      p.lvalue_.accept(new LValueVisitor<R,A>(), arg);
      //p.ident_;
      return null;
    }
  }
  public class MethodCallVisitor<R,A> implements MethodCall.Visitor<R,A>
  {
    public R visit(Latte.Absyn.MCall p, A arg)
    { /* Code For MCall Goes Here */
      p.lvalue_.accept(new LValueVisitor<R,A>(), arg);
      p.functioncall_.accept(new FunctionCallVisitor<R,A>(), arg);
      return null;
    }
  }
  public class LValueVisitor<R,A> implements LValue.Visitor<R,A>
  {
    public R visit(Latte.Absyn.LVFCall p, A arg)
    { /* Code For LVFCall Goes Here */
      p.functioncall_.accept(new FunctionCallVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.LVMethodCall p, A arg)
    { /* Code For LVMethodCall Goes Here */
      p.methodcall_.accept(new MethodCallVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.LVSimpleIdent p, A arg)
    { /* Code For LVSimpleIdent Goes Here */
      //p.ident_;
      return null;
    }    public R visit(Latte.Absyn.LVEArrAccess p, A arg)
    { /* Code For LVEArrAccess Goes Here */
      p.arraccess_.accept(new ArrAccessVisitor<R,A>(), arg);
      return null;
    }    public R visit(Latte.Absyn.LVAttrAcc p, A arg)
    { /* Code For LVAttrAcc Goes Here */
      p.attraccess_.accept(new AttrAccessVisitor<R,A>(), arg);
      return null;
    }
  }
  public class AddOpVisitor<R,A> implements AddOp.Visitor<R,A>
  {
    public R visit(Latte.Absyn.Plus p, A arg)
    { /* Code For Plus Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Minus p, A arg)
    { /* Code For Minus Goes Here */
      return null;
    }
  }
  public class MulOpVisitor<R,A> implements MulOp.Visitor<R,A>
  {
    public R visit(Latte.Absyn.Times p, A arg)
    { /* Code For Times Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Div p, A arg)
    { /* Code For Div Goes Here */
      return null;
    }    public R visit(Latte.Absyn.Mod p, A arg)
    { /* Code For Mod Goes Here */
      return null;
    }
  }
  public class RelOpVisitor<R,A> implements RelOp.Visitor<R,A>
  {
    public R visit(Latte.Absyn.LTH p, A arg)
    { /* Code For LTH Goes Here */
      return null;
    }    public R visit(Latte.Absyn.LE p, A arg)
    { /* Code For LE Goes Here */
      return null;
    }    public R visit(Latte.Absyn.GTH p, A arg)
    { /* Code For GTH Goes Here */
      return null;
    }    public R visit(Latte.Absyn.GE p, A arg)
    { /* Code For GE Goes Here */
      return null;
    }    public R visit(Latte.Absyn.EQU p, A arg)
    { /* Code For EQU Goes Here */
      return null;
    }    public R visit(Latte.Absyn.NE p, A arg)
    { /* Code For NE Goes Here */
      return null;
    }
  }
}